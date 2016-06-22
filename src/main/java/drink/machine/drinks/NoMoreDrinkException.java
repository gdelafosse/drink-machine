package drink.machine.drinks;

import javax.ws.rs.core.Response;

import drink.machine.DrinkMachineException;

public class NoMoreDrinkException extends DrinkMachineException
{
    public NoMoreDrinkException(String drinkName) {
        super(String.format("The drink '%s' is no more available.", drinkName));
    }

    @Override
    public Response.Status getStatus()
    {
        return Response.Status.NOT_FOUND;
    }
}
