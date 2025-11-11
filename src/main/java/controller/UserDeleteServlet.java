package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.SessionKeys;

import java.io.IOException;

@WebServlet("/user/delete")
public class UserDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain; charset=UTF-8");
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.getWriter().write("NO_SESSION");
            return;
        }

        // ✅ 세션에서 로그인 아이디 가져오기
        String userId = (String) session.getAttribute(SessionKeys.LOGIN_USERID);
        if (userId == null || userId.isEmpty()) {
            resp.getWriter().write("NO_USER");
            return;
        }

        boolean deleted = dao.deleteUserById(userId);

        if (deleted) {
            session.invalidate();
            resp.getWriter().write("SUCCESS");
        } else {
            resp.getWriter().write("FAIL");
        }
    }
}
