package app.servlets;

import app.JSONParser;
import app.exceptions.*;
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
            model.addNewUser(login, pass, rePass);

            logger.info("user '" + login + "' is saved to file");

            resp.getWriter().print("0");

        } catch (InvalidInputDataException e) {

            resp.setStatus(401);

            HashMap<String, Object> root = new HashMap<>();

            Template tmp = model.getFTLConfig().getTemplate("error_input.ftl");

            String error_msg = e.getLocalizedMessage();

            if (e instanceof PasswordsDoNotMatchException)
                error_msg = "passwords do not match";

            if (e instanceof UserExistsException)
                error_msg = "user already exists";

            if (e instanceof ShortLoginException)
                error_msg = "login must consist at least 3 symbols";

            if (e instanceof ShortPasswordException)
                error_msg = "password must consist at least 8 symbols";

            root.put("error_msg", error_msg);

            try {
                tmp.process(root, resp.getWriter());
            } catch (TemplateException ex) {
                ex.printStackTrace();
            }

            logger.warn("failed to sign up as \"" + login + "\": " + error_msg);
        }
    }
}
