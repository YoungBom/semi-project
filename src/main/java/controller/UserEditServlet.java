package controller;

import dao.UserDAO;
import model.User; // ✅ User 사용하므로 import 필요
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/edit")
public class UserEditServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	// GET: 수정 폼 표시
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession s = req.getSession(false);
		Object uidObj = (s == null) ? null : s.getAttribute("LOGIN_UID");
		if (uidObj == null) {
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
			return;
		}

		int uid = (uidObj instanceof Integer) ? (Integer) uidObj : Integer.parseInt(uidObj.toString());

		try {
			User me = userDao.findByPk(uid);
			if (me == null) {
				// 세션은 있는데 DB에는 없는 경우 → 세션 정리 후 로그인으로
				s.invalidate();
				resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
				return;
			}
			req.setAttribute("me", me); // 폼에 미리 채워 넣기
			req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	// POST: 수정 저장
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		HttpSession s = req.getSession(false);
		Object uidObj = (s == null) ? null : s.getAttribute("LOGIN_UID");
		if (uidObj == null) {
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
			return;
		}
		int uid = (uidObj instanceof Integer) ? (Integer) uidObj : Integer.parseInt(uidObj.toString());

		String email = req.getParameter("email");
		String nickname = req.getParameter("nickname");
		String phone = req.getParameter("phone");
		String birth = req.getParameter("birth");
		String gender = req.getParameter("gender");
		String address = req.getParameter("address");

		try {
			int rows = userDao.updateProfile(uid, email, nickname, phone, birth, gender, address);
			if (rows == 1) {
				resp.sendRedirect(req.getContextPath() + "/user/mypage?updated=1");
			} else {
				req.setAttribute("error", "수정에 실패했습니다.");
				// 실패 시에도 사용자가 입력한 값이 보이도록 다시 셋업 (선택)
				User me = userDao.findByPk(uid);
				req.setAttribute("me", me);
				req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
			}
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}
}
