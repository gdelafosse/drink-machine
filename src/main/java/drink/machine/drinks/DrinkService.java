package drink.machine.drinks;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.NotFoundException;

@Stateless
public class DrinkService {

    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager em;

    public Drink getDrinkByName(String drinkName) {
        Drink drink = em.find(Drink.class, drinkName);
        assertDrink(drink, drinkName);
        em.detach(drink);
        return drink;
    }

    public void pop(String drinkName) {
        Drink drink = em.find(Drink.class, drinkName);
        assertDrink(drink, drinkName);
        drink.setAmount(drink.getAmount() - 1);
    }

    private void assertDrink(Drink drink, String drinkName) {
        if (drink == null) {
            throw new NotFoundException(String.format("The drink called '%s' is no more available.", drinkName));
        }
    }
}
