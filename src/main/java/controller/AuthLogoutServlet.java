package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;

@WebServlet("/logout")
public class AuthLogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s != null) s.invalidate();
        // ★ 여기서 contextPath만 보내지 말고 목적지까지 명시
        resp.sendRedirect(req.getContextPath() + "/login");   // or "/main.jsp"
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 헤더에서 GET으로 호출해도 안전하게 동작하도록
        doPost(req, resp);
    }
}