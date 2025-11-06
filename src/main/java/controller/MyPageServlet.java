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
        Object uidObj = (s == null) ? null : s.getAttribute(SessionKeys.LOGIN_UID);
        if (uidObj == null) {
            String next = URLEncoder.encode(req.getRequestURI(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/login?next=" + next);
            return;
        }

        long uid = (uidObj instanceof Number)
                ? ((Number) uidObj).longValue()
                : Long.parseLong(uidObj.toString());

        try {
            UserDTO user = userDao.findById(uid);
            if (user == null) {
                s.invalidate();
                String next = URLEncoder.encode(req.getRequestURI(), StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/login?next=" + next);
                return;
            }

            // JSP에서 어떤 이름을 쓰든 보이도록 둘 다 세팅
            req.setAttribute("user", user);
            req.setAttribute("me", user);

            req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}