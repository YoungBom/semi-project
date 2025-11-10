package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/check-id")
public class CheckIdServlet extends HttpServlet {
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String userId = req.getParameter("user_id");
        boolean available = false;

        if (userId != null && !userId.isBlank()) {
            // ✅ DB에 아이디가 존재하지 않으면 사용 가능(true)
            available = !dao.existsByUserId(userId);
        }
        

        
        String json = String.format("{\"available\": %s}", available);
        resp.getWriter().write(json);
    }
}
