
package controller;

import dao.UserDAO;
import util.PasswordUtil;

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
			int uid = Math.toIntExact(Long.parseLong(uidStr));
			String hash = PasswordUtil.hash(newPw);
			userDao.updatePasswordHash(uid, hash); // id 기반 오버로드 사용
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp?reset=ok");
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("uid", uidStr);
			req.setAttribute("error", "비밀번호 변경 중 오류가 발생했습니다.");
			req.getRequestDispatcher("/user/reset_password.jsp").forward(req, resp);
		}
	}
}
