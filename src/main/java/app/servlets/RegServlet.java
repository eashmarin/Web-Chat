package app.servlets;

import app.JSONParser;
import app.entities.User;
import app.exceptions.UserExistsException;
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

        HashMap<String, Object> root = new HashMap<>();

        Template tmp = model.getConfiguration().getTemplate("error_input.ftl");

        try {
            if (pass.equals(rePass)) {
                model.addUser(new User(login, pass));

                logger.info("user \'" + login + "\' is saved to file");

                resp.getWriter().print("0");
            }
            else {
                resp.setStatus(401);

                String error_msg = "passwords do not match";

                logger.warn("failed to sign up as \'" + login + "\': " + error_msg);

                root.put("error_msg", error_msg);

                tmp.process(root, resp.getWriter());
            }

        } catch (UserExistsException e) {

            resp.setStatus(401);

            String error_msg = "user \"" + login + "\" already exists";

            logger.warn("failed to sign up: " + error_msg);

            root.put("error_msg", error_msg);

            try {
                tmp.process(root, resp.getWriter());
            } catch (TemplateException ex) {
                ex.printStackTrace();
            }

        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
