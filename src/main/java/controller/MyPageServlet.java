package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/user/mypage")
public class MyPageServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession s = req.getSession(false);
        Long uidObj = (s == null) ? null : toLong(s.getAttribute(SessionKeys.LOGIN_UID));

        if (uidObj == null) {
            String next = URLEncoder.encode(req.getRequestURI(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/login?next=" + next);
            return;
        }

        long uid = uidObj.longValue();
        try {
            UserDTO user = userDao.findById(uid); // findById가 없다면 findByPk로 바꾸고 DAO에 맞춰주세요.
            if (user == null) {
                s.invalidate();
                String next = URLEncoder.encode(req.getRequestURI(), StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/login?next=" + next);
                return;
            }
            req.setAttribute("user", user);
            req.getRequestDispatcher("/resources/user/mypage.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Long toLong(Object o) {
        if (o == null) return null;
        if (o instanceof Long) return (Long) o;
        if (o instanceof Integer) return ((Integer) o).longValue();
        if (o instanceof Number) return ((Number) o).longValue();
        try { return Long.parseLong(o.toString()); } catch (Exception ignore) { return null; }
    }
}