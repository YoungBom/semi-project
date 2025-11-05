package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/mypage")
public class MyPageServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		Object uidObj = (session == null) ? null : session.getAttribute("LOGIN_UID");
		if (uidObj == null) {
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
			return;
		}

		int uid;
		try {
			uid = (uidObj instanceof Integer) ? (Integer) uidObj : Integer.parseInt(uidObj.toString());
		} catch (Exception e) {
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
			return;
		}

		try {
			User me = userDao.findByPk(uid);
			if (me == null) {
				// 세션은 있는데 사용자가 DB에 없음 → 안전하게 로그아웃 처리
				session.invalidate();
				resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
				return;
			}
			req.setAttribute("me", me);
			req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}
}
