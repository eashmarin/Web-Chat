package app.exceptions;

public class PasswordsDoNotMatchException extends InvalidInputDataException {
    public PasswordsDoNotMatchException(String msg) {
        super(msg);
    }
}
