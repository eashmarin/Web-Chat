package app.exceptions;

public class UserExistsException extends ChatException{
    public UserExistsException(String msg) {
        super(msg);
    }
}
