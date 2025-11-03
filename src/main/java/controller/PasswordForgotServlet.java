
package controller;

import controller.PasswordChangeServlet;
import dao.UserDao;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@WebServlet("/password/forgot")
public class PasswordForgotServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();
    private final PasswordResetDao resetDao = new PasswordResetDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/password_forgot.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginId = req.getParameter("user_id"); // 또는
        String email   = req.getParameter("email");

        try {
            User u = null;
            if (email != null && !email.isBlank()) {
                u = userDao.findByEmail(email);
            } else if (loginId != null && !loginId.isBlank()) {
                u = userDao.findByLoginId(loginId);
            }

            // 보안상: 존재하지 않아도 "전송했다"라고 응답 (계정 유무 노출 방지)
            if (u != null) {
                String token = generateToken();
                Instant expires = Instant.now().plusSeconds(60 * 60); // 1시간 유효
                resetDao.createToken(u.getId(), token, expires);

                // 실제 서비스는 이메일 발송. 개발 단계에서는 콘솔 출력.
                System.out.println("[RESET LINK] " + req.getRequestURL().toString().replace("/forgot","")
                        + "/reset?token=" + token);
            }

            req.setAttribute("msg", "비밀번호 재설정 안내가 전송되었습니다. 메일함(또는 콘솔)을 확인하세요.");
            req.getRequestDispatcher("/user/password_forgot.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private String generateToken() {
        byte[] b = new byte[32];
        new SecureRandom().nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }
}
