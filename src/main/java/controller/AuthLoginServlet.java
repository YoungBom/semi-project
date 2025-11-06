// controller/AuthLoginServlet.java
package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.PasswordUtil;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/login")
public class AuthLoginServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String id = req.getParameter("user_id");
		String pw = req.getParameter("user_pw");
		String remember = req.getParameter("remember_me");

		UserDTO u = userDao.findByLoginId(id);
		boolean ok = (u != null) && PasswordUtil.verify(pw, u.getPasswordHash());
		if (!ok) {
			req.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
			req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
			return;
		}

		// 세션 재발급
		HttpSession old = req.getSession(false);
		if (old != null)
			old.invalidate();
		HttpSession s = req.getSession(true);
		s.setAttribute(SessionKeys.LOGIN_UID, u.getId());
		s.setAttribute(SessionKeys.LOGIN_NAME, u.getNickname() != null ? u.getNickname() : u.getName());
		s.setAttribute(SessionKeys.LOGIN_ROLE, u.getRole() != null ? u.getRole() : "USER");

		// 선택: remember_me 쿠키
		if ("1".equals(remember)) {
			Cookie c = new Cookie("remember_user", URLEncoder.encode(u.getUserId(), StandardCharsets.UTF_8));
			c.setHttpOnly(true);
			c.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
			c.setMaxAge(60 * 60 * 24 * 14); // 14일
			resp.addCookie(c);
		}

		resp.sendRedirect(req.getContextPath() + "/main.jsp");
	}
}
