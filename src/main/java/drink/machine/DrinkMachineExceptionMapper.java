package drink.machine;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import drink.machine.buy.NotEnoughMoneyGivenException;
import drink.machine.buy.NotEnoughMoneyInMachineException;
import drink.machine.drinks.DrinkNotFoundException;
import drink.machine.drinks.NoMoreDrinkException;

@Provider
public class DrinkMachineExceptionMapper implements ExceptionMapper<DrinkMachineException>
{
    private static Map<Class<? extends DrinkMachineException>, Response.Status> statuses = new HashMap<>();

    static {
        statuses.put(NotEnoughMoneyInMachineException.class, Response.Status.PAYMENT_REQUIRED);
        statuses.put(NotEnoughMoneyGivenException.class, Response.Status.PAYMENT_REQUIRED);
        statuses.put(DrinkNotFoundException.class, Response.Status.NOT_FOUND);
        statuses.put(NoMoreDrinkException.class, Response.Status.NOT_FOUND);
    }

    @Override
    public Response toResponse(final DrinkMachineException exception)
    {
        Class<?> c = exception.getClass();
        Response.Status status = statuses.get(c);
        if (status == null) {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return Response.status(status)
            .entity(exception.getMessage())
            .build();
    }
}
