package drink.machine.buy;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import drink.machine.DrinkMachineException;
import drink.machine.coins.Coins;
import drink.machine.coins.CoinsJsonAdapter;

@Stateless
@Path("/buy")
public class BuyResource
{
    @EJB
    private BuyService buyService;

    /**
     * 200 success and returns the money back
     * 402 if there's not enough money
     * 404 if product doesn't exist
     * 404 if there's no more drink
     * 503 if there's not enough money into the machine to returns the money back
     *
     * @param drinkName the name of the drink
     * @param moneyJson the money given
     * @return the money back
     */
    @POST
    @Path("/{drinkName}")
    public Response buy(@PathParam("drinkName") String drinkName, JsonObject moneyJson)
    {
        try
        {
            Coins back = buyService.buy(drinkName, CoinsJsonAdapter.toCoins(moneyJson));
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
