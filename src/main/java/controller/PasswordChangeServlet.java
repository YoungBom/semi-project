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
    private final UserDao dao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer uid = (Integer) req.getSession().getAttribute("LOGIN_UID");
        if (uid == null) { resp.sendRedirect(req.getContextPath()+"/user/login.jsp"); return; }
        req.getRequestDispatcher("/user/password_change.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer uid = (Integer) req.getSession().getAttribute("LOGIN_UID");
        if (uid == null) { resp.sendRedirect(req.getContextPath()+"/user/login.jsp"); return; }

        String current = req.getParameter("current_password");
        String npw     = req.getParameter("new_password");
        String confirm = req.getParameter("new_password_confirm");

        if (npw == null || !npw.equals(confirm)) {
            req.setAttribute("error","새 비밀번호가 일치하지 않습니다.");
            doGet(req, resp); return;
        }

        try {
            User me = dao.findByPk(uid);
            if (me == null || !PasswordUtil.verify(current, me.getUser_pw())) {
                req.setAttribute("error","현재 비밀번호가 올바르지 않습니다.");
                doGet(req, resp); return;
            }
            dao.updatePassword(uid, PasswordUtil.hash(npw));

            // 세션 재발급
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();
            req.getSession(true).setAttribute("LOGIN_UID", me.getId());

            resp.sendRedirect(req.getContextPath()+"/mypage");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
