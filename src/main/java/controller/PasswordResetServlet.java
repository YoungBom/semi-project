package controller;

import dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/password/reset")
public class PasswordResetServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("token", req.getParameter("token"));
		req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String token = req.getParameter("token");
		String pw1 = req.getParameter("new_pw");
		String pw2 = req.getParameter("new_pw2");

		if (token == null || token.isBlank()) {
			req.setAttribute("error", "토큰이 유효하지 않습니다.");
			req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
			return;
		}
		if (pw1 == null || pw1.length() < 8) {
			req.setAttribute("error", "비밀번호는 8자 이상이어야 합니다.");
			req.setAttribute("token", token);
			req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
			return;
		}
		if (!pw1.equals(pw2)) {
			req.setAttribute("error", "비밀번호가 일치하지 않습니다.");
			req.setAttribute("token", token);
			req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
			return;
		}

		boolean ok = userDao.updatePasswordByToken(token, pw1);
		if (!ok) {
			req.setAttribute("error", "토큰이 만료되었거나 이미 사용되었습니다.");
			req.setAttribute("token", token);
			req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
			return;
		}

		req.setAttribute("msg", "비밀번호가 재설정되었습니다. 새 비밀번호로 로그인하세요.");
		req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
	}
}
