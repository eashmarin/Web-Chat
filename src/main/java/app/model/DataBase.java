package app.model;

import app.entities.User;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private static DataBase instance = new DataBase();

    private static final String dbUrl = "jdbc:postgresql://ec2-34-247-72-29.eu-west-1.compute.amazonaws.com:5432/de2cf9805u5mit";
    private static final String dbUser = "ompdbabhwsjxcb";
    private static final String dbPassword = "cd22373249502279cdb8caa501247cec6880c52c598aca7d09a3be0f754a3e71";

    private static Connection con;
    private static PreparedStatement createStatement;
    private static PreparedStatement insertStatement;
    private static PreparedStatement selectStatement;
    private static ResultSet rs;

    private DataBase() {
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

            selectStatement = con.prepareStatement("SELECT login, password FROM USERS");

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
            insertStatement.setString(1, user.getName());
            insertStatement.setInt(2, user.getEncodedPass());

            createStatement.executeUpdate();
            insertStatement.executeUpdate();

            close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                insertStatement.close();
                createStatement.close();
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
