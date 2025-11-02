package controller;

import dao.UserDao;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/user/edit")
public class UserEditServlet extends HttpServlet {
	private final UserDao dao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer uid = (Integer) req.getSession().getAttribute("LOGIN_UID");
		if (uid == null) {
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
			return;
		}
		try {
			User me = dao.findByPk(uid);
			req.setAttribute("me", me);
			req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer uid = (Integer) req.getSession().getAttribute("LOGIN_UID");
		if (uid == null) {
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
			return;
		}

		String email = req.getParameter("email");
		String nickname = req.getParameter("nickname");
		String phone = req.getParameter("phone");
		String birth = req.getParameter("birth");
		String gender = req.getParameter("gender");
		String address = req.getParameter("address");

		try {
			if (dao.existsEmailExceptMe(email, uid)) {
				req.setAttribute("error", "이미 사용 중인 이메일입니다.");
				doGet(req, resp);
				return;
			}
			if (dao.existsNicknameExceptMe(nickname, uid)) {
				req.setAttribute("error", "이미 사용 중인 닉네임입니다.");
				doGet(req, resp);
				return;
			}
			dao.updateProfile(uid, email, nickname, phone, birth, gender, address);
			resp.sendRedirect(req.getContextPath() + "/mypage");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
