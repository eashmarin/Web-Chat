package app.model;


import app.entities.Message;
import app.entities.User;
import app.exceptions.*;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private static final Model instance = new Model();

    Configuration cfg;
    private final Map<String, User> users;
    private final List<Message> messages;
    private final List<User> usersOnline;

    public static Model getInstance() {
        return instance;
    }

    private Model() {
        users = new HashMap<>();
        messages = new ArrayList<>();
        usersOnline = new ArrayList<>();

        cfg = new Configuration(Configuration.VERSION_2_3_31);
        try {
            cfg.setDirectoryForTemplateLoading(new File(getClass().getResource("/templates").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(getClass().getResource("/users.csv").getPath()));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] pair = line.split(",");

                users.put(pair[0], new User(pair[0], pair[1]));
            }

            reader.close();

            LogManager.getRootLogger().debug("users: " + users.keySet());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfiguration() {
        return cfg;
    }

    public void addNewUser(String login, String password, String rePassword) throws UserExistsException, PasswordsDoNotMatchException, SmallPasswordException, ShortLoginException {
        if (login.length() < 3)
            throw new ShortLoginException(Integer.toString(login.length()));

        if (password.length() < 8)
            throw new SmallPasswordException(Integer.toString(password.length()));

        if (!password.equals(rePassword))
            throw new PasswordsDoNotMatchException("");

        if (users.containsKey(login))
            throw new UserExistsException(login);

        users.put(login, new User(login, password));

        try {
            FileWriter writer = new FileWriter(getClass().getResource("/users.csv").getPath(), true);

            writer.append('\n');
            writer.append(login + "," + password);

            writer.close();
        } catch (IOException e) {
            LogManager.getRootLogger().error("error while writing user data to file", e);
        }
    }

    public void makeOnline(User user) {
        boolean contains = (usersOnline.stream()
                .anyMatch(u -> u.getName().equals(user.getName())));

        if (!contains) {
            usersOnline.add(user);

            LogManager.getRootLogger().info("user \'" + user + "\' became online");
        }
    }

    public void logIn(User user) throws InvalidInputDataException {
        if (getUsers().containsKey(user.getName()) && isUserValid(user))
            makeOnline(user);
        else
            throw new InvalidInputDataException(user.getName());
    }


    public void addMessage(Message message) {
        messages.add(message);
    }

    public boolean isUserValid(User user) {
        return users.get(user.getName()).getPass() == user.getPass();
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public List<Message> getMessages() { return messages; }

    public List<Message> getMessagesAfter(Message message) {

        if (message == null)
            return getMessages();                           // TODO: change the way

        int fromIndex = getMessages().indexOf(message) + 1;
        int toIndex = getMessages().size();

        return getMessages().subList(fromIndex, toIndex);
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

        LogManager.getRootLogger().info("user \'" + username + "\' became offline");
    }
}
