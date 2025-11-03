package controller;

import dao.PasswordResetDao;
import dao.UserDao;
import model.User;
import util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/password/reset")
public class PasswordResetServlet extends HttpServlet {
    private final PasswordResetDao resetDao = new PasswordResetDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null || token.isBlank()) {
            req.setAttribute("error", "유효하지 않은 요청입니다.");
            req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
            return;
        }
        try {
            var rt = resetDao.findValidToken(token);
            if (rt == null) {
                req.setAttribute("error", "토큰이 만료되었거나 이미 사용되었습니다.");
            }
            req.setAttribute("token", token);
            req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token   = req.getParameter("token");
        String npw     = req.getParameter("new_password");
        String confirm = req.getParameter("new_password_confirm");

        if (npw == null || !npw.equals(confirm)) {
            req.setAttribute("error", "새 비밀번호가 일치하지 않습니다.");
            req.setAttribute("token", token);
            req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
            return;
        }

        try {
            var rt = resetDao.findValidToken(token);
            if (rt == null) {
                req.setAttribute("error", "토큰이 만료되었거나 이미 사용되었습니다.");
                req.setAttribute("token", token);
                req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
                return;
            }

            User u = userDao.findByPk(rt.userId);
            if (u == null) {
                req.setAttribute("error", "사용자 정보를 찾을 수 없습니다.");
                req.setAttribute("token", token);
                req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
                return;
            }

            userDao.updatePassword(u.getId(), PasswordUtil.hash(npw));
            resetDao.markUsed(rt.id);

            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
