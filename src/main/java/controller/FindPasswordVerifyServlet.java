package controller;

import dao.UserDAO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/find_password")
public class FindPasswordVerifyServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	// GET으로 들어오면 폼 보여주기 (직접 접근 대비)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/user/find_password.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String userId = trim(req.getParameter("user_id"));
		String email = trim(req.getParameter("email"));

		// 실패 시 입력값 유지
		req.setAttribute("user_id", userId);
		req.setAttribute("email", email);

		if (userId.isEmpty() || email.isEmpty()) {
			req.setAttribute("error", "아이디와 이메일을 모두 입력하세요.");
			req.getRequestDispatcher("/user/find_password.jsp").forward(req, resp);
			return;
		}

		// 디버그용 로그 (서버 콘솔에서 실제 값 확인)
		System.out.printf("[FindPW] user_id='%s', email='%s'%n", userId, email);

		try {
			UserDTO u = userDao.findByLoginIdAndEmail(userId, email);
			if (u == null) {
				req.setAttribute("error", "일치하는 회원 정보를 찾을 수 없습니다.");
				req.getRequestDispatcher("/user/find_password.jsp").forward(req, resp);
				return;
			}

			// 검증 통과 → 세션 플래그/정보 저장 (세션 방식)
			HttpSession s = req.getSession(true);
			s.setAttribute("RESET_OK", Boolean.TRUE); // 재설정 허용 플래그
			s.setAttribute("RESET_UID", u.getUserId()); // user_id 문자열 (updatePasswordHash에 사용)

			// (선택) 보강: TTL/CSRF
			s.setAttribute("RESET_ISSUED_AT", System.currentTimeMillis()); // 예: ResetPasswordServlet에서 10분 체크
			String csrf = java.util.UUID.randomUUID().toString().replace("-", "");
			s.setAttribute("RESET_CSRF", csrf);

			// 재설정 페이지로 이동 (CSRF 전달하려면 쿼리스트링/hidden으로 넘겨)
			resp.sendRedirect(req.getContextPath() + "/user/reset_password.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "처리 중 오류가 발생했습니다.");
			req.getRequestDispatcher("/user/find_password.jsp").forward(req, resp);
		}
	}

	private static String trim(String s) {
		return (s == null) ? "" : s.trim();
	}
}
