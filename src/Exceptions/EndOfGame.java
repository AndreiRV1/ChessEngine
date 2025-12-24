package Exceptions;

public class EndOfGame extends RuntimeException {
    public EndOfGame(String message) {
        super(message);
    }
}
