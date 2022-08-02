package app.model;

import app.entities.*;
import app.exceptions.*;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model {
    private static final Model instance = new Model();

    private final Map<String, User> users;
    private final List<Message> messages;
    private final List<User> usersOnline;

    private final DataBase db = DataBase.getInstance();

    public static Model getInstance() {
        return instance;
    }

    private Model() {
        messages = new ArrayList<>();
        usersOnline = new ArrayList<>();

        users = db.getUsers();

        LogManager.getRootLogger().debug("users: " + users.keySet());
    }

    public void addNewUser(String login, String password, String rePassword) throws UserExistsException, PasswordsDoNotMatchException, ShortPasswordException, ShortLoginException {
        if (login.length() < 3)
            throw new ShortLoginException(Integer.toString(login.length()));

        if (password.length() < 8)
            throw new ShortPasswordException(Integer.toString(password.length()));

        if (!password.equals(rePassword))
            throw new PasswordsDoNotMatchException();

        if (users.containsKey(login))
            throw new UserExistsException(login);

        User user = new User(login, password);

        users.put(login, user);

        db.saveUser(user);
    }

    public void makeOnline(User user) {
        boolean contains = (usersOnline.stream()
                .anyMatch(u -> u.getName().equals(user.getName())));

        if (!contains) {
            usersOnline.add(user);

            LogManager.getRootLogger().info("user '" + user + "' became online");
        }
    }

    public void authorizeUser(User user) throws InvalidInputDataException {
        if (getUsers().containsKey(user.getName()) && isUserValid(user))
            makeOnline(user);
        else
            throw new InvalidInputDataException(user.getName());
    }


    public void addMessage(Message message) {
        messages.add(message);
    }

    private boolean isUserValid(User user) {
        return users.get(user.getName()).getEncodedPass() == user.getEncodedPass();
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public List<Message> getMessagesStartingFromNextTo(Message message) {
        if (message == null)
            return messages;

        int fromIndex = messages.indexOf(message) + 1;
        int toIndex = messages.size();

        return messages.subList(fromIndex, toIndex);
    }

    public List<Message> getRecentMessages() throws NoMessagesException {
        if (messages.isEmpty())
            throw new NoMessagesException("");

        int size = messages.size();

        if (size < 5)
            return messages;
        else
            return messages.subList(size - 5, size);
    }

    public Message lastMessage() throws NoMessagesException {
        if (messages.size() == 0)
            throw new NoMessagesException("");
        return messages.get(messages.size() - 1);
    }

    public List<User> getUsersOnline() {
        return usersOnline;
    }

    public void makeOffline(String username) {
        User user = usersOnline.stream()
                .filter(x -> x.getName().equals(username))
                .findFirst()
                .get();

        usersOnline.remove(user);

        LogManager.getRootLogger().info("user '" + username + "' became offline");
    }
}
