package app.ewarehouse.exception;
public class CustomEntityNotFoundException extends RuntimeException {

    public CustomEntityNotFoundException() {
        super("Entity not found");
    }

    public CustomEntityNotFoundException(String message) {
        super(message);
    }
}
