package Exceptions;

public class InvalidLoginCredentials extends RuntimeException {
    public InvalidLoginCredentials(String message) {
        super(message);
    }
}
