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

        // remember-me 등 쿠키 정리 필요 시 여기서 처리 (현재는 생략)

        // 컨텍스트 경로 고려하여 로그인 페이지로 이동
        String ctx = req.getContextPath();
        resp.sendRedirect(((ctx == null) ? "" : ctx) + "/main");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doLogout(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doLogout(req, resp);
    }
}
