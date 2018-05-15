package drink.machine.buy;

import drink.machine.DrinkMachineException;

public class NotEnoughMoneyGivenException extends DrinkMachineException
{
    public NotEnoughMoneyGivenException(int drinkPrice, int given) {
        super(String.format("You didn't put enough money, %d is required and you have given only %d.", drinkPrice, given));
    }
}
