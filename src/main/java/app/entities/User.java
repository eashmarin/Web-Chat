package app.entities;

import app.exceptions.NoMessagesException;

public class User {
    private String name;
    private String password;
    private Message lastSeenMsg;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Message getLastSeenMsg() throws NoMessagesException {
        if (lastSeenMsg == null)
            throw new NoMessagesException("");

        return lastSeenMsg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastSeenMsg(Message message) {
        lastSeenMsg = message;
    }

    @Override
    public String toString() {
        return name;
    }
}
