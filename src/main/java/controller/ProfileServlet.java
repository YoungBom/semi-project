package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/profile")
public class ProfileServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Integer uid = (s != null) ? (Integer) s.getAttribute("LOGIN_UID") : null;
        if (uid == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        try {
            UserDTO u = userDao.findById(uid);
            req.setAttribute("user", u);
            req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
