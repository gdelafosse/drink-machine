package drink.machine.drinks;

import drink.machine.DrinkMachineException;

public class DrinkNotFoundException extends DrinkMachineException
{
    public DrinkNotFoundException(String drinkName) {
        super(String.format("The drink called '%s' is no more available.", drinkName));
    }
}
