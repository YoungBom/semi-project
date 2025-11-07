package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.PasswordUtil;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login") // ★ 반드시 /login
public class AuthLoginServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 실제 JSP 경로로 forward
        req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("user_id");
        String pw = req.getParameter("user_pw");

        try {
            UserDTO u = userDao.findByLoginId(id);
            boolean ok = (u != null) && PasswordUtil.verify(pw, u.getPasswordHash());
            if (!ok) {
                req.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
                req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
                return;
            }

            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();
            HttpSession s = req.getSession(true);
            s.setAttribute(SessionKeys.LOGIN_UID,  u.getId().intValue());
            s.setAttribute(SessionKeys.LOGIN_NAME, (u.getNickname()!=null && !u.getNickname().isEmpty()) ? u.getNickname() : u.getName());
            s.setAttribute(SessionKeys.LOGIN_ROLE, (u.getRole()!=null) ? u.getRole() : "USER");

            String next = req.getParameter("next");
            if (next != null && !next.isBlank()) resp.sendRedirect(req.getContextPath() + next);
            else resp.sendRedirect(req.getContextPath() + "/main");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}