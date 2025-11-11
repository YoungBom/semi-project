package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/security_setup")
public class SecuritySetupServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	// JSP EL이 읽을 수 있도록 public static + public getters
	public static class Question {
		private final int id;
		private final String text;

		public Question(int id, String text) {
			this.id = id;
			this.text = text;
		}

		public int getId() {
			return id;
		}

		public String getText() {
			return text;
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("questions", defaultQuestions());
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
		int uid = Integer.parseInt(String.valueOf(s.getAttribute("LOGIN_UID")));

		String qidStr = req.getParameter("question_id");
		String answer = req.getParameter("answer");
		String customTx = req.getParameter("question_tx"); // 직접 입력 시만 사용

		if (isBlank(qidStr) || isBlank(answer)) {
			req.setAttribute("error", "질문과 답변을 모두 입력해 주세요.");
			req.setAttribute("questions", defaultQuestions());
			req.getRequestDispatcher("/user/security_setup.jsp").forward(req, resp);
			return;
		}

		int qid = Integer.parseInt(qidStr);
		String qText = (qid == 9) ? customTx : getQuestionTextByIdFromDefault(qid);

		if (isBlank(qText)) {
			req.setAttribute("error", "선택한 질문이 올바르지 않거나 직접입력 내용이 비어 있습니다.");
			req.setAttribute("questions", defaultQuestions());
			req.setAttribute("selectedQid", qid);
			req.setAttribute("answer", answer);
			req.getRequestDispatcher("/user/security_setup.jsp").forward(req, resp);
			return;
		}

		boolean ok = userDao.upsertSecurityQA(uid, qid, qText, answer);
		if (ok)
			req.setAttribute("msg", "보안질문이 저장되었습니다.");
		else
			req.setAttribute("error", "저장 중 문제가 발생했습니다.");

		req.setAttribute("questions", defaultQuestions());
		req.setAttribute("selectedQid", qid);
		req.setAttribute("answer", "");
		req.getRequestDispatcher("/user/security_setup.jsp").forward(req, resp);
	}

	// ----------------- 헬퍼 -----------------
	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	/** security_question 테이블이 없어도 동작하도록 기본 목록 제공 */
	private List<Question> defaultQuestions() {
		return java.util.Arrays.asList(new Question(1, "가장 기억에 남는 선생님의 성함은?"), new Question(2, "첫 반려동물의 이름은?"),
				new Question(3, "어린 시절 살던 동네는?"), new Question(4, "졸업한 초등학교 이름은?"), new Question(5, "가장 좋아하는 음식은?"),
				new Question(9, "직접 입력(사용자 정의)"));
	}

	private String getQuestionTextByIdFromDefault(int id) {
		for (Question q : defaultQuestions())
			if (q.getId() == id)
				return q.getText();
		return null;
	}
}
