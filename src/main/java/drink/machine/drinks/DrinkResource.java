package drink.machine.drinks;

import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Stateless
@Path("/drinks")
@Consumes("application/json")
@Produces("application/json")
public class DrinkResource
{
    @EJB
    private DrinkService drinkService;

    @GET
    public Collection<Drink> listDrinks()
    {
        return drinkService.listDrinks().values;
    }

    @GET
    @Path("/{name}")
    public Response getDrink(@PathParam("name") String name) throws DrinkNotFoundException
    {
        return Response.ok(drinkService.getDrink(name)).build();
    }

    @POST
    public Response addDrink(Drink drink)
    {
        return Response.status(Response.Status.CREATED).entity(drinkService.addDrink(drink)).build();
    }


    @PUT
    @Path("/{name}/amount")
    public Drink updateDrink(@PathParam("name") String name, int amount) throws InterruptedException
    {
        return drinkService.updateDrink(name, amount);
    }
}
