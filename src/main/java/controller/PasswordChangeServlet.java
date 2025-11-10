package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/password/change")
public class PasswordChangeServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		HttpSession s = req.getSession(false);
		if (s == null || s.getAttribute(SessionKeys.LOGIN_USERID) == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		String loginUserId = (String) s.getAttribute(SessionKeys.LOGIN_USERID);

		String currentPw = req.getParameter("current_pw");
		String newPw = req.getParameter("new_pw");
		String newPw2 = req.getParameter("new_pw2");

		if (newPw == null || newPw.length() < 8 || !newPw.equals(newPw2)) {
			req.setAttribute("error", "새 비밀번호를 다시 확인하세요.");
			req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
			return;
		}

		Optional<UserDTO> opt = userDao.findByLoginId(loginUserId);
		if (opt.isEmpty()) {
			req.setAttribute("error", "사용자를 찾을 수 없습니다.");
			req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
			return;
		}
		UserDTO me = opt.get();
		String stored = (me.getPwHash() != null ? me.getPwHash() : me.getUserPw());
		if (!PasswordUtil.verify(currentPw, stored)) {
			req.setAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
			req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
			return;
		}

		boolean ok = userDao.changePassword(me.getId(), newPw);
		if (!ok) {
			req.setAttribute("error", "비밀번호 변경에 실패했습니다.");
		} else {
			req.setAttribute("msg", "비밀번호가 변경되었습니다.");
		}
		req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
	}
}
