package app.servlets;

import app.FTLManager;
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

        FTLManager ftlManager = FTLManager.getInstance();

        try {
            ftlManager.putParameter("msgs", model.getRecentMessages());

            model.getUsers().get(author).setLastSeenMsg(model.lastMessage());
        } catch (NoMessagesException e) {
            LogManager.getRootLogger().info("no messages had been before user '" + author + "' enter the chat");
        }

        ftlManager.executeTemplate("chat.ftl", resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Model model = Model.getInstance();

        String author = (String) req.getSession().getAttribute("login");
        String msgText = req.getReader().readLine();

        if (msgText == null || !msgText.matches(".*[a-zA-Z0-9!@#$%^&*(){}_=\\-+?/:\\[\\]'\"].*")) {
            resp.setStatus(204);
            return;
        }

        Message message = new Message(author, msgText);

        model.addMessage(message);
        model.getUsers().get(author).setLastSeenMsg(message);

        FTLManager ftlManager = FTLManager.getInstance();

        ftlManager.putParameter("own_msg", message);
        ftlManager.executeTemplate("message.ftl", resp.getWriter());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String author = (String) req.getSession().getAttribute("login");

        Model model = Model.getInstance();

        try {
            Message lastSeenMsg = model.getUsers().get(author).getLastSeenMsg();

            if (lastSeenMsg != model.lastMessage()) {

                FTLManager ftlManager = FTLManager.getInstance();

                ftlManager.putParameter("msgs", model.getMessagesStartingFromNextTo(lastSeenMsg));
                ftlManager.executeTemplate("message.ftl", resp.getWriter());

                model.getUsers().get(author).setLastSeenMsg(model.lastMessage());
            }
        } catch (NoMessagesException e) {
            //LogManager.getRootLogger().info("no messages to update for user \'" + author + "\'");
        }
    }
}
