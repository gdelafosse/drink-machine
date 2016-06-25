package drink.machine.drinks;

import drink.machine.DrinkMachineException;

public class NoMoreDrinkException extends DrinkMachineException
{
    public NoMoreDrinkException(String drinkName) {
        super(String.format("The drink '%s' is no more available.", drinkName));
    }
}
