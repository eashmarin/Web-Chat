package app.exceptions;

public class SmallPasswordException extends InvalidInputDataException{
    public SmallPasswordException(String msg) {
        super(msg);
    }
}
