package drink.machine;

import javax.ws.rs.core.Response.Status;

public abstract class DrinkMachineException extends Exception {
    public DrinkMachineException(String message) {
        super(message);
    }

    public abstract Status getStatus();
}
