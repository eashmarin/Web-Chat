package app.servlets;

import app.FTLManager;
import app.JSONParser;
import app.entities.User;
import app.exceptions.InvalidInputDataException;
import app.model.Model;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class AddServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/add.html");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Logger logger = LogManager.getRootLogger();

        String input = req.getReader().readLine();
        String login = JSONParser.getParameter(input, "login");
        String pass = JSONParser.getParameter(input, "password");

        Model model = Model.getInstance();

        User user = new User(login, pass);

        try {
            model.authorizeUser(user);

            logger.info("user '" + user + "' logged in");

            req.getSession().setAttribute("login", login);

        } catch (InvalidInputDataException e) {

            resp.setStatus(401);

            String error_msg = "data is invalid";

            FTLManager ftlManager = FTLManager.getInstance();

            ftlManager.putParameter("error_msg", error_msg);
            ftlManager.executeTemplate("error_input.ftl", resp.getWriter());

            logger.warn("failed to log in as '" + user + "': " + error_msg);
        }
    }
}
