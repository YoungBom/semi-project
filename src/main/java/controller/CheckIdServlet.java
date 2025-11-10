package controller;

import dao.UserDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/id/check")
public class CheckIdServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setHeader("Cache-Control", "no-store");

        String uid = req.getParameter("user_id");
        boolean available = (uid != null && !uid.isBlank()) && !userDao.existsByLoginId(uid.trim());

        resp.getWriter().write("{\"available\":" + available + "}");
    }
}
