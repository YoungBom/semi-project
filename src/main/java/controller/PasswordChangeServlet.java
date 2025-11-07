package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/change-password")
public class PasswordChangeServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/user/change_password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession s = req.getSession(false);
        Integer uid = (s != null) ? (Integer) s.getAttribute("LOGIN_UID") : null;
        String loginId = (s != null) ? (String) s.getAttribute("LOGIN_ID") : null;

        if (uid == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        String cur = req.getParameter("current_pw");
        String nw  = req.getParameter("new_pw");
        String nw2 = req.getParameter("new_pw2");

        try {
            UserDTO u = (loginId != null) ? userDao.findByLoginId(loginId) : userDao.findById(uid);
            if (u == null) throw new ServletException("user not found");

            if (!PasswordUtil.verify(cur, u.getPwHash())) {
                req.setAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
                req.getRequestDispatcher("/user/change_password.jsp").forward(req, resp);
                return;
            }
            if (nw == null || nw.length() < 8 || !nw.equals(nw2)) {
                req.setAttribute("error", "새 비밀번호가 규칙에 맞지 않거나 일치하지 않습니다.");
                req.getRequestDispatcher("/user/change_password.jsp").forward(req, resp);
                return;
            }

            String hash = PasswordUtil.hash(nw);
            userDao.updatePassword(uid, hash);

            s.setAttribute("FLASH_SUCCESS", "비밀번호가 변경되었습니다.");
            resp.sendRedirect(req.getContextPath() + "/main.jsp"); // 요구사항 반영
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
