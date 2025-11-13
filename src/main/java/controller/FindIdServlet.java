package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/user/find_id")
public class FindIdServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String step = req.getParameter("step");
		try {
			if ("answer".equals(step)) {
				// 2단계: 보안질문 답 검증
				String uidStr = req.getParameter("uid");
				String answer = req.getParameter("answer");
				if (isBlank(uidStr) || isBlank(answer)) {
					req.setAttribute("error", "요청이 올바르지 않습니다.");
					forward(req, resp, "/user/find_id.jsp");
					return;
				}

				int uid = safeToInt(uidStr);

				boolean ok = userDao.verifySecurityAnswer(uid, answer);
				if (!ok) {
					req.setAttribute("error", "보안 질문의 답변이 일치하지 않습니다.");
					req.setAttribute("step", "answer");
					req.setAttribute("uid", uid);
					req.setAttribute("question_text", nullToEmpty(userDao.getSecurityQuestionText(uid)));
					forward(req, resp, "/user/find_id.jsp");
					return;
				}

				// 정답 → 아이디 마스킹 표시
				Optional<UserDTO> uOpt = userDao.findById(uid);
				if (uOpt.isEmpty()) {
					req.setAttribute("error", "사용자 정보를 찾을 수 없습니다.");
					forward(req, resp, "/user/find_id.jsp");
					return;
				}
				String loginId = uOpt.get().getUserId();
				String masked = maskLoginId(loginId);

				req.setAttribute("maskedUserId", masked);
				forward(req, resp, "/user/find_id_result.jsp");
				return;
			}

			// 1단계: 휴대폰 번호로 사용자 찾기
			String phone = req.getParameter("phone");
			if (isBlank(phone)) {
				req.setAttribute("error", "휴대폰 번호를 입력해 주세요.");
				forward(req, resp, "/user/find_id.jsp");
				return;
			}
			String phoneDigits = phone.replaceAll("[^0-9]", "");

			Optional<UserDTO> uOpt = userDao.findByPhoneOnly(phoneDigits);
			if (uOpt.isEmpty()) {
				req.setAttribute("error", "일치하는 회원 정보를 찾을 수 없습니다.");
				forward(req, resp, "/user/find_id.jsp");
				return;
			}

			// DTO의 id가 Integer든 Long이든 안전하게 변환
			int uid = safeToInt(uOpt.get().getId());

			String qText = userDao.getSecurityQuestionText(uid);
			if (isBlank(qText)) {
				req.setAttribute("error", "보안 질문이 설정되어 있지 않습니다. 마이페이지에서 먼저 설정해 주세요.");
				forward(req, resp, "/user/find_id.jsp");
				return;
			}

			req.setAttribute("step", "answer");
			req.setAttribute("uid", uid);
			req.setAttribute("question_text", qText);
			forward(req, resp, "/user/find_id.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "처리 중 오류가 발생했습니다.");
			forward(req, resp, "/user/find_id.jsp");
		}
	}

	// ---------- helpers ----------
	private void forward(HttpServletRequest req, HttpServletResponse resp, String jsp)
			throws ServletException, IOException {
		req.getRequestDispatcher(jsp).forward(req, resp);
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	private String nullToEmpty(String s) {
		return s == null ? "" : s;
	}

	/** Long/Integer/String 모두 안전하게 int로 변환 */
	private int safeToInt(Object v) {
		if (v instanceof Integer)
			return (Integer) v;
		if (v instanceof Long)
			return Math.toIntExact((Long) v);
		if (v instanceof String)
			return Math.toIntExact(Long.parseLong((String) v));
		if (v instanceof Number)
			return Math.toIntExact(((Number) v).longValue());
		return Math.toIntExact(Long.parseLong(String.valueOf(v)));
	}

	private String maskLoginId(String id) {
		if (id == null || id.length() < 3)
			return "***";
		int keep = Math.min(3, id.length());
		StringBuilder sb = new StringBuilder(id.substring(0, keep));
		for (int i = keep; i < id.length(); i++)
			sb.append('*');
		return sb.toString();
	}
}
