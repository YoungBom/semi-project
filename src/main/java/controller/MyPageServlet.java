package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;

@WebServlet("/user/mypage")
public class MyPageServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Integer uid = (s == null) ? null : (Integer)s.getAttribute("LOGIN_UID");
        if (uid == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login.jsp?next=" +
                    java.net.URLEncoder.encode(req.getRequestURI(), java.nio.charset.StandardCharsets.UTF_8));
            return;
        }
        try {
            UserDTO user = userDao.findById(uid);
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/jsp/user/mypage.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
