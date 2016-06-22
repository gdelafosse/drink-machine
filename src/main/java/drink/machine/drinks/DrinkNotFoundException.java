package drink.machine.drinks;

import javax.ws.rs.core.Response;

import drink.machine.DrinkMachineException;

public class DrinkNotFoundException extends DrinkMachineException
{
    public DrinkNotFoundException(String drinkName) {
        super(String.format("The drink called '%s' is no more available.", drinkName));
    }

    @Override
    public Response.Status getStatus()
    {
        return Response.Status.NOT_FOUND;
    }
}
