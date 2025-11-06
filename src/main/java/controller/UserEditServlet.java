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

@WebServlet("/user/edit")
public class UserEditServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession s = req.getSession(false);
        Long uid = (s == null) ? null : toLong(s.getAttribute(SessionKeys.LOGIN_UID));
        if (uid == null) {
            String next = URLEncoder.encode("/user/edit", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/login?next=" + next);
            return;
        }

        try {
            // ★ DAO 표준 메서드명 사용
            UserDTO me = userDao.findById(uid);
            if (me == null) {
                s.invalidate();
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            req.setAttribute("me", me);
            req.setAttribute("user", me); // JSP 호환용 별칭
            req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8"); // 폼 인코딩
        HttpSession s = req.getSession(false);
        Long uid = (s == null) ? null : toLong(s.getAttribute(SessionKeys.LOGIN_UID));
        if (uid == null) {
            String next = URLEncoder.encode("/user/edit", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/login?next=" + next);
            return;
        }

        String email    = req.getParameter("email");
        String nickname = req.getParameter("nickname");
        String phone    = req.getParameter("phone");
        String birth    = req.getParameter("birth");
        String gender   = req.getParameter("gender");
        String address  = req.getParameter("address");

        try {
            int rows = userDao.updateProfile(uid, email, nickname, phone, birth, gender, address);
            if (rows == 1) {
                resp.sendRedirect(req.getContextPath() + "/user/mypage?updated=1");
                return;
            }
            req.setAttribute("error", "수정에 실패했습니다.");

            UserDTO me = userDao.findById(uid);
            req.setAttribute("me", me);
            req.setAttribute("user", me);
            req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Number/문자열 모두 안전하게 Long으로
    private Long toLong(Object o) {
        if (o == null) return null;
        if (o instanceof Long) return (Long) o;
        if (o instanceof Integer) return ((Integer) o).longValue();
        if (o instanceof Number) return ((Number) o).longValue();
        try { return Long.parseLong(o.toString()); } catch (Exception ignore) { return null; }
    }
}