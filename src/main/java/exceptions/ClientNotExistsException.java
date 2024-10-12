package exceptions;

public class ClientNotExistsException extends Exception {
    public ClientNotExistsException() {
        super("Client doesn't exists.");
    }

    public ClientNotExistsException(String message) {
        super(message);
    }
}
