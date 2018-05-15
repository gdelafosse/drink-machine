package drink.machine.graphql;

public class Criteria {
    public Operator operator;
    public String property;
    public String argument;

    public enum Operator {
        EQ
    }
}
