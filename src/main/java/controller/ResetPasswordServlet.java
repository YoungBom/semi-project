package controller;

import dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/reset_password")
public class ResetPasswordServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		String uidStr = req.getParameter("uid");
		String newPw = req.getParameter("new_pw");
		String confirm = req.getParameter("new_pw_confirm");

		// 1) 기본 검증
		if (uidStr == null || newPw == null || confirm == null) {
			req.setAttribute("error", "요청이 올바르지 않습니다.");
			req.getRequestDispatcher("/user/reset_password.jsp").forward(req, resp);
			return;
		}

		if (!newPw.equals(confirm)) {
			req.setAttribute("uid", uidStr);
			req.setAttribute("error", "비밀번호가 일치하지 않습니다.");
			req.getRequestDispatcher("/user/reset_password.jsp").forward(req, resp);
			return;
		}

		try {
			int uid = Integer.parseInt(uidStr);

			// 🔥 여기서는 절대로 해시하지 않고 '평문'을 그대로 넘긴다.
			boolean ok = userDao.updatePasswordHash(uid, newPw);

			if (!ok) {
				req.setAttribute("uid", uidStr);
				req.setAttribute("error", "비밀번호 변경에 실패했습니다.");
				req.getRequestDispatcher("/user/reset_password.jsp").forward(req, resp);
				return;
			}

			// 성공 시 로그인 페이지로 이동
			resp.sendRedirect(req.getContextPath() + "/login?reset=ok");
		} catch (NumberFormatException e) {
			req.setAttribute("error", "요청이 올바르지 않습니다.");
			req.getRequestDispatcher("/user/reset_password.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("uid", uidStr);
			req.setAttribute("error", "비밀번호 변경 중 오류가 발생했습니다.");
			req.getRequestDispatcher("/user/reset_password.jsp").forward(req, resp);
		}
	}
}
