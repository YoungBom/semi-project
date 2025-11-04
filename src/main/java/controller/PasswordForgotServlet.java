package controller;

import dao.PasswordResetDAO;
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
	private final PasswordResetDAO resetDao = new PasswordResetDAO();
	private final UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/user/password_forgot.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String email = req.getParameter("email");

		try {
			User u = userDao.findByEmail(email);
			if (u != null) {
				String token = generateToken();
				resetDao.createToken(u.getId(), token, Instant.now().plusSeconds(60 * 30)); // 30분
				// 실제로는 이메일 발송. 여기서는 화면에 안내만.
				req.setAttribute("debug_token", token);
			}
			req.setAttribute("msg", "비밀번호 재설정 안내가 전송되었습니다.");
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
