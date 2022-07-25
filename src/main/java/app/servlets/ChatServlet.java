package app.servlets;

import app.entities.Message;
import app.exceptions.NoMessagesException;
import app.model.Model;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class ChatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Model model = Model.getInstance();

        String author = (String) req.getSession().getAttribute("login");

        if (author == null) {
            resp.setStatus(403);
            return;
        }

        Template temp = model.getConfiguration().getTemplate("/chat.ftl");

        Map<String, Object> root = new HashMap<>();

        try {
            root.put("msgs", model.getRecentMessages());

            model.getUsers().get(author).setLastSeenMsg(model.lastMessage());
        } catch (NoMessagesException e) {
            LogManager.getRootLogger().info("no messages had been before user \'" + author + "\' enter the chat");
        }

        Writer out = resp.getWriter();

        try {
            temp.process(root, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Model model = Model.getInstance();

        String author = (String) req.getSession().getAttribute("login");
        String msgText = req.getReader().readLine();
        Message message = new Message(author, msgText);

        Map<String, Object> root = new HashMap<>();

        model.addMessage(message);

        model.getUsers().get(author).setLastSeenMsg(message);

        root.put("own_msg", message);

        Template temp = model.getConfiguration().getTemplate("/message.ftl");

        Writer out = resp.getWriter();

        try {
            temp.process(root, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String author = (String) req.getSession().getAttribute("login");

        Model model = Model.getInstance();

        try {
            Message lastSeenMsg = model.getUsers().get(author).getLastSeenMsg();

            if (lastSeenMsg != model.lastMessage()) {

                Template temp = model.getConfiguration().getTemplate("/message.ftl");

                Map<String, Object> root = new HashMap<>();

                Writer out = resp.getWriter();

                root.put("msgs", model.getMessagesAfter(lastSeenMsg));

                temp.process(root, out);

                model.getUsers().get(author).setLastSeenMsg(model.lastMessage());
            }
        } catch (NoMessagesException e) {
            //LogManager.getRootLogger().info("no messages to update for user \'" + author + "\'");
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
