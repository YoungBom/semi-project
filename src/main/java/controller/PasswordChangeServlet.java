package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.PasswordUtil;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/password-change")
public class PasswordChangeServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession s = req.getSession(false);
		if (s == null || s.getAttribute(SessionKeys.LOGIN_UID) == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		long uid = (long) s.getAttribute(SessionKeys.LOGIN_UID);

		String currentPw = req.getParameter("current_pw");
		String newPw = req.getParameter("new_pw");

		try {
			UserDTO me = userDao.findByPk(uid);
			if (me == null || !PasswordUtil.verify(currentPw, me.getPasswordHash())) {
				req.setAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
				req.getRequestDispatcher("/user/password_change.jsp").forward(req, resp);
				return;
			}
			String newHash = PasswordUtil.hash(newPw);
			userDao.updatePassword(uid, newHash);

			req.setAttribute("msg", "비밀번호가 변경되었습니다.");
			req.getRequestDispatcher("/user/password_change.jsp").forward(req, resp);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}
}
