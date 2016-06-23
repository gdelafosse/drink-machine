package drink.machine.drinks;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class DrinkService {

    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager em;

    public Collection<Drink> listDrinks()
    {
        TypedQuery<Drink> result = em.createQuery("SELECT d FROM Drink d", Drink.class);
        return result.getResultList();
    }

    public Drink addDrink(Drink drink) {
        em.persist(drink);
        return drink;
    }

    public Drink updateDrink(String drinkName, int amount)
    {
        Drink drink = em.find(Drink.class, drinkName);
        drink.setAmount(amount);
        return drink;
    }

    public Drink getDrink(String drinkName) throws DrinkNotFoundException
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
