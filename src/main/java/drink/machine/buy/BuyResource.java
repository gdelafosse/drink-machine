package drink.machine.buy;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import drink.machine.coins.Coin;
import drink.machine.coins.Coins;
import drink.machine.coins.CoinsJsonAdapter;
import drink.machine.coins.CoinsService;
import drink.machine.drinks.Drink;
import drink.machine.drinks.DrinkService;

@Stateless
@Path("/buy")
public class BuyResource
{
    @EJB
    private DrinkService drinkService;

    @EJB
    private CoinsService coinsService;

    /**
     * 200 success and returns the money back
     * 402 if there's not enough money
     * 404 if product doesn't exist
     * 404 if there's no more drink
     * 503 if there's not enough money into the machine to returns the money back
     *
     * @param drinkName
     * @param moneyJson
     * @return
     */
    @POST
    @Path("/{drinkName}")
    public Response buy(@PathParam("drinkName") String drinkName, JsonObject moneyJson) {
        Drink drink = drinkService.getDrinkByName(drinkName);
        Coins coins = coinsService.getCoins();
        Coins money = CoinsJsonAdapter.toCoins(moneyJson);
        if (drink == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(String.format("The drink called '%s' doesn't exist.", drinkName))
                    .build();
        } else if (drink.getAmount() == 0 ) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(String.format("The drink called '%s' is no more available.", drinkName))
                    .build();
        } else if (drink.getPrice() > money.sum()) {
            return Response.status(Response.Status.PAYMENT_REQUIRED)
                    .entity(String.format("You didn't put enough money, %d is required.", drink.getPrice()))
                    .build();
        } else {
            try
            {
                return Response.ok(CoinsJsonAdapter.toJson(buy(drink,money))).build();
            }
            catch (NotEnoughMoneyInMachineException e)
            {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("There's not enough money in the machine.")
                        .build();
            }
        }
    }

    /**
     * Returns the money to give back when buying the drink and given the money.
     * The method updates the amount of coins in the machine.
     * The method updates the amount of drinks in the machine.
     * @param drink
     * @param money
     * @return
     * @throws NotEnoughMoneyInMachineException
     */
    private Coins buy(Drink drink, Coins money) throws NotEnoughMoneyInMachineException
    {
        // get the money available in the machine
        Coins coins = coinsService.getCoins();
        // add what the user gave
        coins.add(money);

        // process the money to give back
        Coins back = new Coins();
        int sold = money.sum() - drink.getPrice();

        // we can assume that sold is >= 0 because we checked it before
        // if the sold is 0 we can return directly 'back' which contains 0 coins
        if (sold > 0)
        {
            // for each Coin starting from the biggest we process the amount of coin that we could give back
            // we loop over the Coin enum because the order is descendant
            for (Coin coin : Coin.values())
            {
                int amount = back(sold, coin.value, coins.getCoin(coin));
                sold = sold - amount * coin.value;

                back.setCoin(coin, amount);

                // we can break if the sold is 0
                if (sold == 0)
                    break;
            }

            // if sold is not 0 that means that there's not enough money in the machine
            if (sold != 0)
            {
                throw new NotEnoughMoneyInMachineException();
            }
        }

        // update the number of pieces in the machine
        coins.remove(back);
        coinsService.setCoins(coins);

        // remove the drink from the machine
        drinkService.pop(drink.getName());

        return back;
    }

    private int back(int sold, int coinValue, int stock) {
        int amount = 0;
        if (sold >= coinValue) {
            amount = sold/coinValue;
            amount = amount>stock?stock:amount;
        }
        return amount;
    }

    private static class NotEnoughMoneyInMachineException extends Exception {
        NotEnoughMoneyInMachineException() {
            super();
        }
    }
}
