package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class AuthLogoutServlet extends HttpServlet {

    private void doLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 세션 무효화
        HttpSession s = req.getSession(false);
        if (s != null) s.invalidate();

        // remember-me 쿠키 정리(사용 중일 때)
        String ctx = req.getContextPath();
        Cookie rm = new Cookie("REMEMBER", "");
        rm.setMaxAge(0);
        rm.setPath((ctx == null || ctx.isEmpty()) ? "/" : ctx);
        resp.addCookie(rm);

        // 로그인 페이지로 이동
        resp.sendRedirect(((ctx == null) ? "" : ctx) + "/login");
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doLogout(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doLogout(req, resp);
    }
}
