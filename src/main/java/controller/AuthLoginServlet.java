package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class AuthLoginServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("user_id");
        String pw = req.getParameter("user_pw");

        try {
            UserDTO u = userDao.findByLoginId(id);
            boolean ok = (u != null) && PasswordUtil.verify(pw, u.getPwHash());
            if (!ok) {
                req.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
                req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
                return;
            }
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();

            HttpSession s = req.getSession(true);
            s.setAttribute("LOGIN_UID", u.getId());
            s.setAttribute("LOGIN_ID",  u.getUserId());
            s.setAttribute("LOGIN_NAME",u.getName());
            s.setAttribute("LOGIN_ROLE",u.getRole());

            resp.sendRedirect(req.getContextPath() + "/main.jsp");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
