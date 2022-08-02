package app.exceptions;

public class ShortPasswordException extends InvalidInputDataException{
    public ShortPasswordException(String msg) {
        super(msg);
    }
}
