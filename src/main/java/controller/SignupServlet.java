package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/signup") // 필요 시 "/register"로 변경
public class SignupServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserDAO dao = new UserDAO();
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // JSP 파일명이 register.jsp라면 아래 경로를 "/user/register.jsp"로 바꾸세요.
        req.getRequestDispatcher("/user/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userId   = req.getParameter("user_id");
        String pw       = req.getParameter("user_pw");
        String email    = req.getParameter("email");
        String phone    = req.getParameter("phone");
        String birthStr = req.getParameter("birth");   // yyyy-MM-dd
        String gender   = req.getParameter("gender");
        String name     = req.getParameter("name");
        String nickname = req.getParameter("nickname");
        String address  = req.getParameter("address");

        // 기본 검증
        if (userId == null || userId.isBlank()
                || pw == null || pw.length() < 8
                || email == null || email.isBlank()
                || name == null || name.isBlank()) {
            req.setAttribute("error", "필수값을 확인하세요. (비밀번호 8자 이상)");
            req.getRequestDispatcher("/user/signup.jsp").forward(req, resp);
            return;
        }

        // 중복 체크
        if (dao.existsByLoginId(userId) || dao.existsByEmail(email)) {
            req.setAttribute("error", "이미 사용 중인 아이디/이메일입니다.");
            req.getRequestDispatcher("/user/signup.jsp").forward(req, resp);
            return;
        }

        // 생년월일 파싱(LocalDate)
        LocalDate birth = null;
        if (birthStr != null && !birthStr.isBlank()) {
            try {
                birth = LocalDate.parse(birthStr, DF);

                // ✅ 유효성 검사 추가
                LocalDate today = LocalDate.now();
                LocalDate minDate = LocalDate.of(1900, 1, 1); // 예: 1900년 1월 1일 이전 금지

                if (birth.isAfter(today)) {
                    req.setAttribute("error", "생년월일은 오늘 이후일 수 없습니다.");
                    req.getRequestDispatcher("/user/signup.jsp").forward(req, resp);
                    return;
                }

                if (birth.isBefore(minDate)) {
                    req.setAttribute("error", "생년월일이 너무 오래되었습니다. 다시 확인해주세요.");
                    req.getRequestDispatcher("/user/signup.jsp").forward(req, resp);
                    return;
                }

            } catch (Exception e) {
                req.setAttribute("error", "생년월일 형식이 올바르지 않습니다. 예) 1995-01-01");
                req.getRequestDispatcher("/user/signup.jsp").forward(req, resp);
                return;
            }
        }

        // DTO 채우기 (비밀번호 해시는 DAO에서 처리)
        UserDTO u = new UserDTO();
        u.setUserId(userId);
        u.setEmail(email);
        u.setPhone(phone);
        u.setBirth(birth);
        u.setGender(gender);
        u.setName(name);
        u.setNickname(nickname);
        u.setAddress(address);
        u.setRole("USER");

        // 생성 (DAO가 해시 생성/저장)
        int newId = dao.create(u, pw);
        if (newId <= 0) {
            req.setAttribute("error", "회원 가입에 실패했습니다.");
            req.getRequestDispatcher("/user/signup.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("msg", "회원가입이 완료되었습니다. 로그인해주세요.");
        req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
    }
}
