package controller;

import dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/password/forgot")
public class PasswordForgotServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/user/password_forgot.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String idOrEmail = req.getParameter("id_or_email");
		if (idOrEmail == null || idOrEmail.isBlank()) {
			req.setAttribute("error", "아이디 또는 이메일을 입력하세요.");
			req.getRequestDispatcher("/user/password_forgot.jsp").forward(req, resp);
			return;
		}
		Optional<String> tokenOpt = userDao.createPasswordResetTokenByLoginIdOrEmail(idOrEmail.trim(), 30);
		tokenOpt.ifPresent(tok -> req.setAttribute("dev_token", tok)); // 운영에서는 제거하고 메일 발송
		req.setAttribute("sent", true);
		req.getRequestDispatcher("/user/password_forgot.jsp").forward(req, resp);
	}
}
