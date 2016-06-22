package drink.machine.buy;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import drink.machine.DrinkMachineException;
import drink.machine.coins.Coin;
import drink.machine.coins.Coins;
import drink.machine.coins.CoinsJsonAdapter;

@Stateless
@Path("/buying")
public class BuyingResource
{

    @EJB
    private BuyStateful buying;

    /**
     * 201 if coin is accepted and returns the total put so far
     * 400 if the coin is not recognized
     * @param coinName
     * @return
     */
    @POST
    @Path("/coin/{coinName}")
    public Response putCoin(@PathParam("coinName")String coinName) {
        try
        {
            Coins money = buying.putCoin(Coin.valueOf(coinName.toUpperCase()));
            return Response.status(Response.Status.CREATED).entity(money.sum()).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Your coin is not recognized").build();
        }
    }

    /**
     * Try to gets the drink.
     *
     * 200 success and returns the money back
     * 402 if there's not enough money
     * 404 if product doesn't exist
     * 404 if there's no more drink
     * 503 if there's not enough money into the machine to returns the money back
     *
     * @param drinkName the name of the drink.
     * @return the money back.
     */
    @GET
    @Path("/drink/{drinkName}")
    public Response getDrink(@PathParam("drinkName") String drinkName) {
        try
        {
            Coins back = buying.getDrink(drinkName);
            return Response.ok(CoinsJsonAdapter.toJson(back)).build();
        }
        catch (DrinkMachineException e)
        {
            return Response.status(e.getStatus())
                    .entity(e.getMessage())
                    .build();
        }
    }

}
