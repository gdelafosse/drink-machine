package drink.machine.buy;

import javax.ws.rs.core.Response;

import drink.machine.DrinkMachineException;

public class NotEnoughMoneyGivenException extends DrinkMachineException
{
    public NotEnoughMoneyGivenException(int drinkPrice, int given) {
        super(String.format("You didn't put enough money, %d is required and you have given only %d.", drinkPrice, given));
    }

    @Override
    public Response.Status getStatus()
    {
        return Response.Status.PAYMENT_REQUIRED;
    }
}
