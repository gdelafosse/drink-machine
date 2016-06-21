package drink.machine.drinks;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Drink
{
    @Id
    private String name;

    @Version
    private int version;

    private int amount;

    private int price;

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(final int amount)
    {
        this.amount = amount;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(final int price)
    {
        this.price = price;
    }

    public static class Builder
    {
        private Drink drink = new Drink();

        Builder name(String name)
        {
            drink.setName(name);
            return this;
        }

        Builder amount(int amount)
        {
            drink.setAmount(amount);
            return this;
        }

        Builder price(int price)
        {
            drink.setPrice(price);
            return this;
        }

        Drink build()
        {
            return drink;
        }
    }
}
