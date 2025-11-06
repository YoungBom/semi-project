package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");

		// 1) 파라미터
		String userId = n(req.getParameter("user_id"));
		String pw = n(req.getParameter("user_pw"));
		String pw2 = n(req.getParameter("user_pw2"));
		String email = n(req.getParameter("email"));
		String phone = n(req.getParameter("phone"));
		String birth = n(req.getParameter("birth")); // yyyy-MM-dd
		String gender = n(req.getParameter("gender")); // M/F/O
		String name = n(req.getParameter("name"));
		String nickname = n(req.getParameter("nickname"));
		String address = n(req.getParameter("address"));

		// 2) 서버 검증
		if (userId.isBlank() || pw.isBlank() || pw2.isBlank() || email.isBlank() || phone.isBlank() || birth.isBlank()
				|| gender.isBlank() || name.isBlank() || nickname.isBlank()) {
			respond(req, resp, false, "필수 입력값이 누락되었습니다.", null);
			return;
		}
		if (!pw.equals(pw2)) {
			respond(req, resp, false, "비밀번호가 일치하지 않습니다.", null);
			return;
		}
		if (!pw.matches("(?=.*[a-z])(?=.*\\d)[a-z0-9]{8,20}")) {
			respond(req, resp, false, "비밀번호 규칙(소문자+숫자 8~20자)에 맞지 않습니다.", null);
			return;
		}
		if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			respond(req, resp, false, "이메일 형식이 올바르지 않습니다.", null);
			return;
		}
		if (!phone.matches("^01[0-9]{8,9}$")) {
			respond(req, resp, false, "휴대폰 형식이 올바르지 않습니다.", null);
			return;
		}

		try {
			// 아이디 중복 재확인
			if (userDao.existsByLoginId(userId)) {
				respond(req, resp, false, "이미 사용중인 아이디입니다.", null);
				return;
			}

			// 비밀번호 해시
			String pwHash = PasswordUtil.hash(pw);

			// DTO 구성
			UserDTO u = new UserDTO();
			u.setUserId(userId);
			u.setPasswordHash(pwHash);
			u.setEmail(email);
			u.setPhone(phone);
			u.setBirth(birth); // ★ String 그대로 저장 (DAO는 setString으로 저장)
			u.setGender(gender);
			u.setName(name);
			u.setNickname(nickname);
			u.setAddress(address);

			userDao.insert(u);

			// 성공
			respond(req, resp, true, null, "/login?next=/main.jsp");

		} catch (SQLIntegrityConstraintViolationException dup) {
			respond(req, resp, false, "이미 사용중인 아이디(또는 이메일)입니다.", null);
		} catch (Exception e) {
			respond(req, resp, false, "회원가입 처리 중 오류가 발생했습니다.", null);
		}
	}

	private static String n(String s) {
		return s == null ? "" : s.trim();
	}

	/** JSON을 원하면 JSON, 아니면 기존 폼 흐름(리다이렉트/포워드) */
	private void respond(HttpServletRequest req, HttpServletResponse resp, boolean ok, String message,
			String redirectUrl) throws IOException, ServletException {
		String accept = req.getHeader("Accept");
		boolean wantsJson = accept != null && accept.toLowerCase().contains("application/json");

		if (wantsJson) {
			resp.setContentType("application/json; charset=UTF-8");
			if (message == null)
				message = "";
			String json = "{\"ok\":" + ok + (message.isEmpty() ? "" : ",\"message\":\"" + escape(message) + "\"") + "}";
			resp.getWriter().write(json);
			return;
		}

		// 폼 submit(비AJAX)
		if (ok) {
			if (redirectUrl == null)
				redirectUrl = "/login?next=/main.jsp";
			resp.sendRedirect(req.getContextPath() + redirectUrl);
		} else {
			req.setAttribute("error", message);
			req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
		}
	}

	private String escape(String s) {
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
