package drink.machine.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLResolver;
import drink.machine.drinks.DrinkService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryResolver implements GraphQLQueryResolver {
    private DrinkService drinkService;

    public QueryResolver(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    public Drinks drinks(Pagination pagination, List<Sort> sort, List<Criteria> criterias) {
        Drinks drinks = drinkService.listDrinks(pagination, sort, criterias);
        return drinks;
    }
}
