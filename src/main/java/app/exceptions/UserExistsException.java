package app.exceptions;

public class UserExistsException extends InvalidInputDataException{
    public UserExistsException(String msg) {
        super(msg);
    }
}
