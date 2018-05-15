package drink.machine.graphql;

import java.util.List;

public class PaginateResult <T> {
    public List<T> values;
    public Result result;
}
