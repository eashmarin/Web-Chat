package app.servlets;

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
            model.logIn(user);

            logger.info("user \'" + user + "\' logged in");

            req.getSession().setAttribute("login", login);

        } catch (InvalidInputDataException e) {

            resp.setStatus(401);

            HashMap<String, Object> root = new HashMap<>();

            Template tmp = model.getConfiguration().getTemplate("error_input.ftl");

            String error_msg = "data is invalid";

            root.put("error_msg", error_msg);

            try {
                tmp.process(root, resp.getWriter());
            } catch (TemplateException ex) {
                ex.printStackTrace();
            }

            logger.warn("failed to log in as \'" + user + "\': " + error_msg);
        }
    }
}
