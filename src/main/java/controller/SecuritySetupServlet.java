package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/user/security_setup")
public class SecuritySetupServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession s = req.getSession(false);
		if (s == null || s.getAttribute("LOGIN_UID") == null) {
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
			return;
		}
		int uid = (int) s.getAttribute("LOGIN_UID");

		// 현재 설정 조회 (있으면 화면에 바인딩)
		Map<String, Object> cur = userDao.getSecurityQA(uid); // 아래 4)에서 추가
		req.setAttribute("current", cur);

		// 사전 질문 목록
		req.setAttribute("questions", userDao.listSecurityQuestions()); // 아래 4)에서 추가

		req.getRequestDispatcher("/user/security_setup.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		HttpSession s = req.getSession(false);
		if (s == null || s.getAttribute("LOGIN_UID") == null) {
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
			return;
		}
		int uid = (int) s.getAttribute("LOGIN_UID");

		String mode = req.getParameter("mode"); // "preset" or "custom"
		int qid = 0;
		String qText = null;

		if ("custom".equals(mode)) {
			qText = req.getParameter("question_tx"); // 자유 질문
			qid = Integer.parseInt(req.getParameter("qid_custom_fallback")); // 빈 값 방지용(없으면 1 같은 기본)
		} else {
			qid = Integer.parseInt(req.getParameter("qid"));
		}

		String answer = req.getParameter("answer");

		try {
			boolean ok = userDao.upsertSecurityQA(uid, qid, qText, answer); // 아래 4)에서 추가/수정
			if (ok) {
				req.setAttribute("msg", "보안질문이 저장되었습니다.");
			} else {
				req.setAttribute("error", "저장에 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "저장 중 오류가 발생했습니다.");
		}

		// 다시 화면 로딩
		req.setAttribute("current", userDao.getSecurityQA(uid));
		req.setAttribute("questions", userDao.listSecurityQuestions());
		req.getRequestDispatcher("/user/security_setup.jsp").forward(req, resp);
	}
}
