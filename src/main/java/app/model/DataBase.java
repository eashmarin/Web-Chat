package app.model;

import app.entities.User;
import org.apache.logging.log4j.LogManager;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DataBase {
    private static DataBase instance = new DataBase();

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    private static Connection con;
    private static PreparedStatement createStatement;
    private static PreparedStatement insertStatement;
    private static PreparedStatement selectStatement;
    private static ResultSet rs;

    private DataBase() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(getClass().getResource("/credentials.properties").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbUrl = properties.getProperty("url");
        dbUser = properties.getProperty("user");
        dbPassword = properties.getProperty("password");

        LogManager.getRootLogger().info("db credentials: " + dbUrl + "; " + dbUser + "; " + dbPassword);
    }

    public static DataBase getInstance() {
        return instance;
    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Map<String, User> getUsers() {
        HashMap<String, User> users = new HashMap<>();

        try {
            connect();

            createStatement = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id serial PRIMARY KEY, " +
                            "login VARCHAR (50) UNIQUE NOT NULL, " +
                            "password INT NOT NULL " +
                            ");"
            );

            selectStatement = con.prepareStatement("SELECT login, password FROM USERS");

            createStatement.executeUpdate();
            rs = selectStatement.executeQuery();

            while (rs.next()) {
                String login = rs.getString("login");
                Integer encodedPass = rs.getInt("password");

                users.put(login, new User(login, encodedPass));
            }

            close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                selectStatement.close();
                createStatement.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    public void saveUser(User user) {
        try {
            connect();

            insertStatement = con.prepareStatement(
                    "INSERT into users (id, login, password) " +
                            "VALUES (DEFAULT, ?, ?);"
            );
            insertStatement.setString(1, user.getName());
            insertStatement.setInt(2, user.getEncodedPass());

            insertStatement.executeUpdate();

            close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                insertStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
