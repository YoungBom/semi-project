package controller;

import dao.PasswordResetDAO;
import dao.UserDAO;
import util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/password/reset")
public class PasswordResetServlet extends HttpServlet {
	private final PasswordResetDAO resetDao = new PasswordResetDAO();
	private final UserDAO userDao = new UserDAO();

	// 폼 표시 (token 쿼리스트링으로 전달)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = req.getParameter("token");
		req.setAttribute("token", token);
		req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
	}

	// 비밀번호 변경
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String token = req.getParameter("token");
		String npw = req.getParameter("new_pw");
		String npw2 = req.getParameter("new_pw2");

		try {
			if (token == null || token.isBlank()) {
				req.setAttribute("error", "토큰이 없습니다.");
				req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
				return;
			}
			if (npw == null || !npw.equals(npw2)) {
				req.setAttribute("error", "비밀번호가 일치하지 않습니다.");
				req.setAttribute("token", token);
				req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
				return;
			}

			var rt = resetDao.findByToken(token);
			if (rt == null) {
				req.setAttribute("error", "유효하지 않거나 만료된 토큰입니다.");
				req.getRequestDispatcher("/user/password_reset.jsp").forward(req, resp);
				return;
			}

			userDao.updatePassword(rt.userId, PasswordUtil.hash(npw));
			resetDao.markUsed(rt.id);

			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
