package exceptions;

public class ClientExistsException extends Exception {
    public ClientExistsException() {
        super("Client already exists.");
    }

    public ClientExistsException(String message) {
        super(message);
    }
}
