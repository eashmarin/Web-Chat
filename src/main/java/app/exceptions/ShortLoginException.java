package app.exceptions;

public class ShortLoginException extends InvalidInputDataException{
    public ShortLoginException(String msg) {
        super(msg);
    }
}
