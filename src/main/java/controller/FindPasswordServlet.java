
package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/user/find_password")
public class FindPasswordServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String step = req.getParameter("step");
		try {
			if ("question".equals(step)) {
				// 2단계: 보안질문 답 검증
				String uidStr = req.getParameter("uid");
				String answer = req.getParameter("answer");
				if (isBlank(uidStr) || isBlank(answer)) {
					req.setAttribute("error", "요청이 올바르지 않습니다.");
					forward(req, resp, "/user/find_password.jsp");
					return;
				}
				int uid = safeToInt(uidStr);
				if (!userDao.verifySecurityAnswer(uid, answer)) {
					req.setAttribute("step", "question");
					req.setAttribute("uid", uid);
					req.setAttribute("question_text", userDao.getSecurityQuestionText(uid));
					req.setAttribute("error", "보안 질문의 답변이 일치하지 않습니다.");
					forward(req, resp, "/user/find_password.jsp");
					return;
				}
				// 성공 → 재설정 페이지
				req.setAttribute("uid", uid);
				forward(req, resp, "/user/reset_password.jsp");
				return;
			}

			// 1단계: 아이디 + 휴대폰
			String userId = req.getParameter("user_id");
			String phone = req.getParameter("phone");
			if (isBlank(userId) || isBlank(phone)) {
				req.setAttribute("error", "아이디와 휴대폰 번호를 모두 입력해 주세요.");
				forward(req, resp, "/user/find_password.jsp");
				return;
			}

			String phoneDigits = phone.replaceAll("[^0-9]", "");
			Optional<UserDTO> opt = userDao.findByLoginIdAndPhone(userId, phoneDigits);
			if (opt.isEmpty()) {
				req.setAttribute("error", "일치하는 회원 정보를 찾을 수 없습니다.");
				forward(req, resp, "/user/find_password.jsp");
				return;
			}

			UserDTO u = opt.get();
			int uid = safeToInt(u.getId());
			String qText = userDao.getSecurityQuestionText(uid);
			if (isBlank(qText)) {
				req.setAttribute("error", "보안 질문이 설정되어 있지 않습니다. 먼저 설정해 주세요.");
				forward(req, resp, "/user/find_password.jsp");
				return;
			}

			// 보안질문 단계로
			req.setAttribute("step", "question");
			req.setAttribute("uid", uid);
			req.setAttribute("question_text", qText);
			forward(req, resp, "/user/find_password.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "처리 중 오류가 발생했습니다.");
			forward(req, resp, "/user/find_password.jsp");
		}
	}

	private void forward(HttpServletRequest req, HttpServletResponse resp, String jsp)
			throws ServletException, IOException {
		req.getRequestDispatcher(jsp).forward(req, resp);
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	// Object/String → int 안전 변환
	private int safeToInt(Object idObj) {
		if (idObj instanceof Integer)
			return (Integer) idObj;
		if (idObj instanceof Long)
			return Math.toIntExact((Long) idObj);
		return Math.toIntExact(Long.parseLong(String.valueOf(idObj)));
	}
}
