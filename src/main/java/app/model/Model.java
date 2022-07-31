package app.model;


import app.entities.Message;
import app.entities.User;
import app.exceptions.*;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.sql.*;
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

    private final String dbUrl = "jdbc:postgresql://ec2-34-247-72-29.eu-west-1.compute.amazonaws.com:5432/de2cf9805u5mit";
    private final String dbUser = "ompdbabhwsjxcb";
    private final String dbPassword = "cd22373249502279cdb8caa501247cec6880c52c598aca7d09a3be0f754a3e71";

    private static Connection con;
    private static PreparedStatement createStatement;
    private static PreparedStatement insertStatement;
    private static PreparedStatement selectStatement;
    private static ResultSet rs;

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

        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            selectStatement = con.prepareStatement("SELECT login, password FROM USERS");

            rs = selectStatement.executeQuery();

            while (rs.next()) {
                String login = rs.getString("login");
                Integer encodedPass = rs.getInt("password");

                users.put(login, new User(login, encodedPass));
            }

            LogManager.getRootLogger().debug("users: " + users.keySet());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
                selectStatement.close();
                rs.close();
            } catch (SQLException e) {

            }
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
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            createStatement = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "id serial PRIMARY KEY, " +
                    "login VARCHAR (50) UNIQUE NOT NULL, " +
                    "password INT NOT NULL " +
                    ");"
            );

            insertStatement = con.prepareStatement(
                    "INSERT into users (id, login, password) " +
                            "VALUES (DEFAULT, ?, ?);"
            );
            insertStatement.setString(1, users.get(login).getName());
            insertStatement.setInt(2, users.get(login).getEncodedPass());

            createStatement.executeUpdate();
            insertStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
                createStatement.close();
                insertStatement.close();
            } catch (SQLException e) {

            }
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
        return users.get(user.getName()).getEncodedPass() == user.getEncodedPass();
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
