package controller;

import dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/edit")
public class UserEditServlet extends HttpServlet {
	private final UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession s = req.getSession(false);
		if (s == null || s.getAttribute("LOGIN_UID") == null) {
			resp.sendRedirect(req.getContextPath() + "/login?next="
					+ java.net.URLEncoder.encode(req.getContextPath() + "/edit", "UTF-8"));
			return;
		}
		int uid = (int) s.getAttribute("LOGIN_UID");
		try {
			var me = userDao.findByPk(uid);
			req.setAttribute("me", me);
			req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		HttpSession s = req.getSession(false);
		if (s == null || s.getAttribute("LOGIN_UID") == null) {
			resp.sendRedirect(req.getContextPath() + "/login?next="
					+ java.net.URLEncoder.encode(req.getContextPath() + "/edit", "UTF-8"));
			return;
		}
		int uid = (int) s.getAttribute("LOGIN_UID");

		// 폼 파라미터 — edit.jsp의 name과 반드시 일치
		String email = req.getParameter("email");
		String nickname = req.getParameter("nickname");
		String phone = req.getParameter("phone");
		String birth = req.getParameter("birth");
		String gender = req.getParameter("gender");
		String address = req.getParameter("address");

		try {
			// 간단 검증(필요 시 확장)
			if (email == null || email.isBlank() || nickname == null || nickname.isBlank()) {
				req.setAttribute("error", "필수 항목을 확인하세요.");
				var me = userDao.findByPk(uid);
				req.setAttribute("me", me);
				req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
				return;
			}

			userDao.updateProfile(uid, email, nickname, phone, birth, gender, address); // ✅ 구현 필요
			resp.sendRedirect(req.getContextPath() + "/mypage"); // 저장 후 마이페이지로
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
