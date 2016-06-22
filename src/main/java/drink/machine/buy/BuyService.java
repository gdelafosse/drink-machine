package drink.machine.buy;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import drink.machine.coins.Coin;
import drink.machine.coins.Coins;
import drink.machine.coins.CoinsService;
import drink.machine.drinks.Drink;
import drink.machine.drinks.DrinkNotFoundException;
import drink.machine.drinks.DrinkService;
import drink.machine.drinks.NoMoreDrinkException;

@Stateless
public class BuyService {

    @EJB
    private CoinsService coinsService;

    @EJB
    private DrinkService drinkService;

    /**
     * Returns the money to give back when buying the drink and given the money.
     * The method updates the amount of coins in the machine.
     * The method updates the amount of drinks in the machine.
     * @param drinkName
     * @param money
     * @return
     * @throws DrinkNotFoundException
     * @throws NotEnoughMoneyInMachineException
     * @throws NoMoreDrinkException
     * @throws NotEnoughMoneyGivenException
     */
    public Coins buy(String drinkName, Coins money) throws DrinkNotFoundException, NotEnoughMoneyInMachineException, NoMoreDrinkException, NotEnoughMoneyGivenException
    {
        Drink drink = drinkService.getDrink(drinkName);
        if (drink == null) {
            throw new DrinkNotFoundException(drinkName);
        } else if (drink.getAmount() == 0 ) {
            throw new NoMoreDrinkException(drinkName);
        } else if (drink.getPrice() > money.sum()) {
            throw new NotEnoughMoneyGivenException(drink.getPrice(), money.sum());
        } else {

        }
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
                int amount = back(sold, coin.value, coins.get(coin));
                sold = sold - amount * coin.value;

                back.set(coin, amount);

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
}
