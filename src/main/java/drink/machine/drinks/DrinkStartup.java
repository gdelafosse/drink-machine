package drink.machine.drinks;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class DrinkStartup
{
    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager em;

    @PostConstruct
    public void startUp() {
        em.persist(new Drink.Builder().name("coca").amount(20).price(120).build());
        em.persist(new Drink.Builder().name("orangina").amount(20).price(120).build());
        em.persist(new Drink.Builder().name("nestea").amount(20).price(110).build());
        em.persist(new Drink.Builder().name("perrier").amount(20).price(100).build());
        em.persist(new Drink.Builder().name("evian").amount(20).price(90).build());
    }
}
