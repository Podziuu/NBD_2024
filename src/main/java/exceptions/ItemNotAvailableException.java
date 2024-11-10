package exceptions;

public class ItemNotAvailableException extends Exception {
    public ItemNotAvailableException() {
        super("Item not exists");
    }

    public ItemNotAvailableException(String message) {
        super(message);
    }
}