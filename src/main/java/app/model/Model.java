package app.model;


import app.entities.Message;
import app.entities.User;
import app.exceptions.NoMessagesException;
import app.exceptions.UserExistsException;
import app.exceptions.WrongDataException;
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
            cfg.setDirectoryForTemplateLoading(new File("C:/Users/Evgeny/IdeaProjects/ChatApp/web/views"));
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
            reader = new BufferedReader(new FileReader ("C:/Users/Evgeny/IdeaProjects/ChatApp/src/main/resources/users.csv"));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] pair = line.split(",");

                users.put(pair[0], new User(pair[0], pair[1]));
            }

            LogManager.getRootLogger().debug("users: " + users.keySet());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Configuration getConfiguration() {
        return cfg;
    }

    public void addUser(User user) throws UserExistsException {

        if (users.containsKey(user.getName()))
            throw new UserExistsException(user.getName());

        users.put(user.getName(), user);

        try {
            FileWriter writer = new FileWriter("C:/Users/Evgeny/IdeaProjects/ChatApp/src/main/resources/users.csv", true);
            writer.append('\n');
            writer.append(user.getName() + "," + user.getPassword());

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeOnline(User user) {
        boolean contains = (usersOnline.stream()
                .anyMatch(u -> u.getName().equals(user.getName())));

        if (!contains) {
            LogManager.getRootLogger().debug("adding user to online group");
            usersOnline.add(user);
        }
    }

    public void logIn(User user) throws WrongDataException {
        if (getUsers().containsKey(user.getName()) && isUserValid(user))
            makeOnline(user);
        else
            throw new WrongDataException(user.getName());
    }


    public void addMessage(Message message) {
        messages.add(message);
    }

    public boolean isUserValid(User user) {
        return (users.get(user.getName()).getPassword().equals(user.getPassword()));
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

        LogManager.getRootLogger().debug("message - " + message + ": fromIndex = " + fromIndex + "; toIndex = " + toIndex);

        return getMessages().subList(fromIndex, toIndex);
    }

    public List<Message> getRecentMessages() throws NoMessagesException {

        if (messages.isEmpty())
            throw new NoMessagesException("");

        if (messages.size() < 5)
            return messages;
        else
            return messages.subList(0, 4);
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
    }
}
