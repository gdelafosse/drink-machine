package drink.machine.buy;

import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;

import drink.machine.DrinkMachineException;
import drink.machine.coins.Coin;
import drink.machine.coins.Coins;

@Stateful
@StatefulTimeout(value = 1, unit = TimeUnit.SECONDS)
public class BuyStateful
{

    @EJB
    private BuyService buyService;

    private Coins money = new Coins();

    public Coins putCoin(Coin coin)
    {
        money.add(coin);
        return money;
    }

    public Coins getDrink(String drinkName) throws DrinkMachineException
    {
        Coins back = buyService.buy(drinkName, money);
        money.reset();
        return back;
    }

}
