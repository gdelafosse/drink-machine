package drink.machine.drinks;

import drink.machine.graphql.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class DrinkService extends AbstractService<Drink> {

    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager em;

    public Drinks listDrinks() {
        return listDrinks(null, null, null);
    }

    public Drinks listDrinks(Pagination pagination, List<Sort> sort, List<Criteria> criterias)
    {
        return list(Drink.class, Drinks.class, pagination, sort, criterias);
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
