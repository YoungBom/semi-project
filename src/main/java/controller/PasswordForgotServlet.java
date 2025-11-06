package controller;
import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;

@WebServlet("/user/password-forgot")
public class PasswordForgotServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		try {
			UserDTO u = userDao.findByEmail(email);
			// 실제 운영에선 토큰을 DB에 저장하고 이메일 발송
			if (u != null) {
				String token = generateToken();
				req.setAttribute("debug_token", token); // 데모 표시
			}
			req.setAttribute("msg", "비밀번호 재설정 안내를 이메일로 전송했습니다.");
			req.getRequestDispatcher("/user/password_forgot.jsp").forward(req, resp);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	private String generateToken() {
		byte[] buf = new byte[24];
		new SecureRandom().nextBytes(buf);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
	}
}
