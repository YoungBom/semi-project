package controller;

import dao.UserDAO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/find_id")
public class FindIdServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	// GET으로 직접 들어오면 폼 보여주기
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/user/find_id.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		// 파라미터 안전 처리(널가드 + trim)
		String email = toTrim(req.getParameter("email"));
		String nickname = toTrim(req.getParameter("nickname"));

		// 실패 시 입력값 유지
		req.setAttribute("email", email);
		req.setAttribute("nickname", nickname);

		if (email.isEmpty() || nickname.isEmpty()) {
			req.setAttribute("error", "이메일과 닉네임을 모두 입력하세요.");
			req.getRequestDispatcher("/user/find_id.jsp").forward(req, resp);
			return;
		}

		try {
			// DB 공백/대소문자 이슈를 줄이려면: findByEmailAndNickname 에 TRIM / COLLATE 반영한 버전 사용 권장
			UserDTO u = userDao.findByEmailAndNickname(email, nickname);
			if (u == null) {
				req.setAttribute("error", "일치하는 회원 정보를 찾을 수 없습니다.");
				req.getRequestDispatcher("/user/find_id.jsp").forward(req, resp);
				return;
			}

			String masked = maskUserId(u.getUserId());
			req.setAttribute("maskedUserId", masked);
			req.getRequestDispatcher("/user/find_id_result.jsp").forward(req, resp);

		} catch (Exception e) {
			// (선택) 서버 로그
			e.printStackTrace();
			req.setAttribute("error", "처리 중 오류가 발생했습니다.");
			req.getRequestDispatcher("/user/find_id.jsp").forward(req, resp);
		}
	}

	private static String toTrim(String s) {
		return (s == null) ? "" : s.trim();
	}

	private String maskUserId(String id) {
		if (id == null || id.isEmpty())
			return "***";
		int keep = Math.min(4, id.length()); // 최대 4글자 노출
		if (keep < 3 && id.length() >= 3)
			keep = 3; // 최소 3 보장
		StringBuilder sb = new StringBuilder(id.substring(0, keep));
		for (int i = keep; i < id.length(); i++)
			sb.append('*');
		return sb.toString();
	}
}
