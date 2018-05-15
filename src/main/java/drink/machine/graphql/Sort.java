package drink.machine.graphql;

import java.util.List;

public class Sort {
    public SortDirection direction = SortDirection.ASC;
    public String property;

    public enum SortDirection {
        ASC, DESC
    }
}
