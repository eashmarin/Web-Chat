package app.servlets;

import app.JSONParser;
import app.entities.User;
import app.exceptions.WrongDataException;
import app.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/add.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Logger logger = LogManager.getRootLogger();

        logger.debug("doPost() is started");

        String input = req.getReader().readLine();
        String login = JSONParser.getParameter(input, "login");
        String pass = JSONParser.getParameter(input, "password");

        Model model = Model.getInstance();

        User user = new User(login, pass);

        try {
            model.logIn(user);

            req.getSession().setAttribute("login", login);

            resp.getWriter().print("0");

        } catch (WrongDataException e) {
            resp.getWriter().print("user data for user " + user + " isn't correct :(");
        }
    }
}