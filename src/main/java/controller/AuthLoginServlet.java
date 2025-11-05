package controller;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/login")
public class AuthLoginServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("user_id");
        String pw = req.getParameter("user_pw");
        String nextParam = req.getParameter("next");

        if (id != null) id = id.trim();
        if (pw != null) pw = pw.trim();

        try {
            User u = userDao.findByLoginId(id);
            boolean ok = (u != null) && PasswordUtil.verify(pw, u.getUser_pw());

            if (!ok) {
                req.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
                req.setAttribute("next", nextParam); // 실패 시에도 next 유지
                req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
                return;
            }

            // 세션 고정화 방지: 기존 세션을 재사용하며 ID만 교체
            HttpSession s = req.getSession(true);
            req.changeSessionId();

            s.setAttribute("LOGIN_UID", u.getId());
            s.setAttribute("LOGIN_NAME", u.getName());

            String next = (nextParam == null || nextParam.isBlank())
                    ? null
                    : URLDecoder.decode(nextParam, StandardCharsets.UTF_8);

            if (next != null && !next.isBlank()) {
                resp.sendRedirect(next);
            } else {
                resp.sendRedirect(req.getContextPath() + "/");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
    }
}
