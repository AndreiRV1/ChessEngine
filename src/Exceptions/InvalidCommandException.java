package Exceptions;
//Exceptions.InvalidCommandException - dacă utilizatorul introduce o comandă invalidă (de
//exemplu, un format gre s, it pentru mutare, o op t, iune inexistentă în meniu, text acolo
//unde se as, teaptă un număr etc.)

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String message) {
        super(message);
    }
}
