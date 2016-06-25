package drink.machine.buy;

import drink.machine.DrinkMachineException;

public class NotEnoughMoneyInMachineException extends DrinkMachineException
{
    public NotEnoughMoneyInMachineException() {
        super("There's not enough money in the machine.");
    }
}
