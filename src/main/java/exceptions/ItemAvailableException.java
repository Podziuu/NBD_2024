package exceptions;

public class ItemAvailableException extends Exception {
    public ItemAvailableException() {
        super("Item already exists");
    }

    public ItemAvailableException(String message) {
        super(message);
    }
}