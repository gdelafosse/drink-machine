package drink.machine.coins;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import drink.machine.drinks.Drink;

@Singleton
@Startup
public class CoinsStartup
{
    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager em;

    @PostConstruct
    public void startUp() {
        Coins coins = new Coins();
        Arrays.stream(Coin.values()).forEach(coin -> coins.setCoin(coin, 5));
        em.persist(coins);
    }
}
