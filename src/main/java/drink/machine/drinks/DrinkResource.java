package drink.machine.drinks;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Stateless
@Path("/drinks")
@Consumes("application/json")
@Produces("application/json")
public class DrinkResource
{
    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager em;

    @GET
    public Collection<Drink> listDrinks()
    {
        TypedQuery<Drink> result = em.createQuery("SELECT d FROM Drink d", Drink.class);
        return result.getResultList();
    }

    @GET
    @Path("/{name}")
    public Drink getDrink(@PathParam("name") String name) {
        return em.find(Drink.class, name);
    }

    @POST
    public Drink addDrink(Drink drink) {
        em.persist(drink);
        return drink;
    }

    @PUT
    @Path("/{name}")
    public Drink updateDrink(@PathParam("name") String name, Drink drink) throws InterruptedException
    {
        Drink updated = em.merge(drink);
        return updated;
    }

    @PUT
    @Path("/{name}/account")
    public Drink updateDrink(@PathParam("name") String name, int account) throws InterruptedException
    {
        Drink drink = em.find(Drink.class, name);
        drink.setAmount(account);
        return drink;
    }
}
