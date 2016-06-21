package drink.machine.coins;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Stateless
@Path("/coins")
@Consumes("application/json")
@Produces("application/json")
public class CoinsResource {

    @EJB
    private CoinsService coinsService;

    @GET
    public JsonObject getCoins() {
        return CoinsJsonAdapter.toJson(coinsService.getCoins());
    }

}
