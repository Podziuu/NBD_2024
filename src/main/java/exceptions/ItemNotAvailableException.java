package exceptions;

public class ItemNotAvailableException extends Throwable {
    public ItemNotAvailableException() {
        super("Item not exists");
    }

    public ItemNotAvailableException(String message) {
        super(message);
    }
}
