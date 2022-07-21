package app.servlets;

import app.JSONParser;
import app.entities.User;
import app.exceptions.UserExistsException;
import app.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/views/signup.html");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Logger logger = LogManager.getRootLogger();

        String input = req.getReader().readLine();
        String login = JSONParser.getParameter(input, "login");
        String pass = JSONParser.getParameter(input, "password");
        String rePass = JSONParser.getParameter(input, "rePassword");

        Model model = Model.getInstance();

        try {
            if (pass.equals(rePass)) {
                model.addUser(new User(login, pass));

                logger.info("user \'" + login + "\' is saved to file");

                resp.getWriter().print("0");
            }
            else {
                logger.warn("failed to sign up as \'" + login + "\': passwords aren't matched");

                resp.getWriter().print("passwords aren't matched");
            }

        } catch (UserExistsException e) {
            logger.warn("failed to sign up as \'" + login + "\': user already exists");

            resp.getWriter().print("user \"" + login + "\" already exists");
        }
    }
}
