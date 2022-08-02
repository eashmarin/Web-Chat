package app.entities;

import java.util.Date;

public class Message {
    private final String author;
    private final String text;
    private final Date date;

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
        date = new Date();
    }

    @Override
    public String toString() {
        return "[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "]" + author + ": " + text;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() { return  date.getHours() + ":" + date.getMinutes(); }
}
