package app.servlets;

import app.FTLManager;
import app.model.Model;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class UsersOnlineServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Model model = Model.getInstance();

        FTLManager ftlManager = FTLManager.getInstance();

        ftlManager.putParameter("users_online", model.getUsersOnline());
        ftlManager.executeTemplate("users_online.ftl", resp.getWriter());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Model model = Model.getInstance();

        String username = (String) req.getSession().getAttribute("login");

        model.makeOffline(username);

        FTLManager ftlManager = FTLManager.getInstance();

        ftlManager.putParameter("users_online", model.getUsersOnline());
        ftlManager.executeTemplate("users_online.ftl", resp.getWriter());
    }
}
