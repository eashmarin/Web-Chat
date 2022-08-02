package app.exceptions;

public class InvalidInputDataException extends ChatException{
    public InvalidInputDataException() { super(); }
    public InvalidInputDataException(String msg) {
        super(msg);
    }
}
