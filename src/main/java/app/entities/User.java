package app.entities;

import app.exceptions.NoMessagesException;

public class User {
    private String name;
    private int encodedPass;
    private Message lastSeenMsg;

    public User(String name, String password) {
        this.name = name;
        this.encodedPass = password.hashCode();
    }

    public String getName() {
        return name;
    }

    public int getPass() {
        return encodedPass;
    }

    public Message getLastSeenMsg() throws NoMessagesException {
        if (lastSeenMsg == null)
            throw new NoMessagesException("");

        return lastSeenMsg;
    }

    public void setLastSeenMsg(Message message) {
        lastSeenMsg = message;
    }

    @Override
    public String toString() {
        return name;
    }
}
