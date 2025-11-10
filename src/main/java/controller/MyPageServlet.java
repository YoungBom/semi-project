package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/user/mypage")
public class MyPageServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession s = req.getSession(false);
		if (s == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		// 여러 키 대응
		Object uidObj = s.getAttribute(SessionKeys.LOGIN_UID);
		if (uidObj == null)
			uidObj = s.getAttribute("LOGIN_UID");
		if (uidObj == null)
			uidObj = s.getAttribute("LOGIN_USER_ID");
		if (uidObj == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		// ✅ int로 안전 변환 (DAO는 findById(int))
		final int uid;
		try {
			if (uidObj instanceof Number) {
				uid = ((Number) uidObj).intValue();
			} else {
				uid = Integer.parseInt(String.valueOf(uidObj));
			}
		} catch (NumberFormatException e) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		Optional<UserDTO> opt = userDao.findById(uid); // ✅ int 전달
		if (opt.isEmpty()) {
			s.invalidate();
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		req.setAttribute("user", opt.get());
		req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
	}
}
