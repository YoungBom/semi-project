package controller;

import dao.UserDao;
import model.User;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/password/change")
public class PasswordChangeServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("LOGIN_UID") == null) {
            req.getSession(true).setAttribute("AFTER_LOGIN_REDIRECT", req.getRequestURI());
            resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
            return;
        }
        req.getRequestDispatcher("/user/password_change.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("LOGIN_UID") == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
            return;
        }

        int uid = (int) s.getAttribute("LOGIN_UID");
        String cur = req.getParameter("current_pw");
        String npw = req.getParameter("new_pw");
        String npw2 = req.getParameter("new_pw2");

        try {
            if (npw == null || !npw.equals(npw2)) {
                req.setAttribute("error", "새 비밀번호가 일치하지 않습니다.");
                req.getRequestDispatcher("/user/password_change.jsp").forward(req, resp);
                return;
            }

            User me = userDao.findByPk(uid);
            if (me == null) {
                resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
                return;
            }

            // 현재 비번 검증(user_pw 컬럼의 해시와 비교)
            boolean ok = PasswordUtil.verify(cur, me.getUser_pw());
            if (!ok) {
                req.setAttribute("error", "현재 비밀번호가 올바르지 않습니다.");
                req.getRequestDispatcher("/user/password_change.jsp").forward(req, resp);
                return;
            }

            // 새 비번 저장
            String hashed = PasswordUtil.hash(npw);
            userDao.updatePassword(uid, hashed);

            s.setAttribute("FLASH", "비밀번호가 변경되었습니다.");
            resp.sendRedirect(req.getContextPath() + "/user/mypage");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
