package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.SessionKeys;

import java.io.IOException;

@WebServlet("/user/delete")
public class UserDeleteServlet extends HttpServlet {
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain; charset=UTF-8");
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.getWriter().write("NO_SESSION");
            return;
        }

        String sessionUserId = (String) session.getAttribute(SessionKeys.LOGIN_USERID);
        String inputId = req.getParameter("inputId");
        
        
        // 🔥 입력값이 세션의 userId와 일치하는지 확인
        if (inputId == null || !inputId.equals(sessionUserId)) {
            resp.getWriter().write("WRONG_ID");
            return;
        }

        boolean deleted = dao.deleteUserById(sessionUserId);

        if (deleted) {
            session.invalidate();
            resp.getWriter().write("SUCCESS");
        } else {
            resp.getWriter().write("FAIL");
        }
    }
}
