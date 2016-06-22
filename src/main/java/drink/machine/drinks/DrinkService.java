package drink.machine.drinks;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DrinkService {

    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager em;

    public Drink getDrinkByName(String drinkName) throws DrinkNotFoundException
    {
        Drink drink = em.find(Drink.class, drinkName);
        assertDrink(drink, drinkName);
        em.detach(drink);
        return drink;
    }

    public void pop(String drinkName) throws DrinkNotFoundException
    {
        Drink drink = em.find(Drink.class, drinkName);
        assertDrink(drink, drinkName);
        drink.setAmount(drink.getAmount() - 1);
    }

    private void assertDrink(Drink drink, String drinkName) throws DrinkNotFoundException
    {
        if (drink == null) {
            throw new DrinkNotFoundException(drinkName);
        }
    }
}
