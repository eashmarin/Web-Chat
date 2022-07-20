package app.servlets;

import app.JSONParser;
import app.entities.User;
import app.exceptions.UserExistsException;
import app.model.Model;

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
        String input = req.getReader().readLine();
        String login = JSONParser.getParameter(input, "login");
        String pass = JSONParser.getParameter(input, "password");
        String rePass = JSONParser.getParameter(input, "rePassword");

        Model model = Model.getInstance();

        try {
            if (pass.equals(rePass)) {
                model.addUser(new User(login, pass));

                resp.getWriter().print("0");
            }
            else
                resp.getWriter().println("passwords aren't matched");

        } catch (UserExistsException e) {
            resp.getWriter().println("user \"" + login + "\" already exists");
        }
    }
}
