package drink.machine.buy;

import javax.ws.rs.core.Response;

import drink.machine.DrinkMachineException;

public class NotEnoughMoneyInMachineException extends DrinkMachineException
{
    public NotEnoughMoneyInMachineException() {
        super("There's not enough money in the machine.");
    }

    @Override
    public Response.Status getStatus()
    {
        return Response.Status.SERVICE_UNAVAILABLE;
    }
}
