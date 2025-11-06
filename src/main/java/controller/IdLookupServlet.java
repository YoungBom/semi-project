package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/id-lookup")
public class IdLookupServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        try {
            UserDTO u = userDao.findByEmail(email);
            if (u == null) {
                // 보안상 같은 응답 권장
                req.setAttribute("msg", "해당 이메일로 가입된 아이디 안내를 전송했습니다.");
            } else {
                req.setAttribute("result", "아이디: " + mask(u.getUserId()));
            }
            req.getRequestDispatcher("/user/id_lookup.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private String mask(String userId) {
        if (userId == null || userId.length() < 3) return "***";
        int vis = Math.min(3, userId.length());
        return userId.substring(0, vis) + "*".repeat(userId.length() - vis);
    }
}
