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
public class DrinkService {

    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager em;

    public Drinks listDrinks() {
        return listDrinks(null, null);
    }

    public Drinks listDrinks(Pagination pagination, List<Sort> sort)
    {
        return list(Drink.class, Drinks.class, pagination, sort);
    }

    private <T,E extends PaginateResult<T>> E list(Class<T> type, Class<E> result, Pagination pagination, List<Sort> sort) {
        Pagination safePagination = pagination == null? new Pagination() : pagination;

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(type);

        // SELECT T root FROM T
        Root<T> root = query.from(type);
        query.select(root);
        if (sort != null && !sort.isEmpty()) {
            query.orderBy(sort.stream().map(s -> {
                    if (s.direction == Sort.SortDirection.ASC) {
                        return criteriaBuilder.asc(root.get(s.property));
                    } else {
                        return criteriaBuilder.desc(root.get(s.property));
                    }
                }).collect(Collectors.toList())
            );
        }
        E paginateResult = null;
        try {
            paginateResult = result.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        paginateResult.values = em.createQuery(query)
                .setFirstResult(safePagination.offset)
                .setMaxResults(safePagination.limit)
                .getResultList();
        paginateResult.result = new Result();
        paginateResult.result.count = paginateResult.values.size();
        paginateResult.result.offset = safePagination.offset;

        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        count.select(criteriaBuilder.count(count.from(type)));

        paginateResult.result.totalCount = em.createQuery(count).getSingleResult().intValue();
        return paginateResult;
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
