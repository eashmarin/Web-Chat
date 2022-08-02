package app.servlets;

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

        Template template = model.getFTLConfig().getTemplate("users_online.ftl");

        Map<String, Object> root = new HashMap<>();

        root.put("users_online", model.getUsersOnline());

        Writer out = resp.getWriter();

        try {
            template.process(root, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Model model = Model.getInstance();

        String username = (String) req.getSession().getAttribute("login");

        model.makeOffline(username);

        Map<String, Object> root = new HashMap<>();

        root.put("users_online", model.getUsersOnline());

        Writer out = resp.getWriter();

        Template template = model.getFTLConfig().getTemplate("users_online.ftl");

        try {
            template.process(root, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
