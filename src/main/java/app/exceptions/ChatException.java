package app.exceptions;

public class ChatException extends Exception{
    public ChatException() { super(); }
    public ChatException(String msg) {
        super(msg);
    }
}
