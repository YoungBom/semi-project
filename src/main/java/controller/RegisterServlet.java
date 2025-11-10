package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UserDAO dao = new UserDAO();
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userId   = req.getParameter("user_id");
        String pw       = req.getParameter("user_pw");
        String email    = req.getParameter("email");
        String phone    = req.getParameter("phone");
        String birthStr = req.getParameter("birth"); // yyyy-MM-dd
        String gender   = req.getParameter("gender");
        String name     = req.getParameter("name");
        String nickname = req.getParameter("nickname");
        String address  = req.getParameter("address");

        // 1) 필수값 체크
        if (userId == null || userId.isBlank()
                || pw == null || pw.length() < 8
                || email == null || email.isBlank()
                || name == null || name.isBlank()) {
            req.setAttribute("error", "필수값을 확인하세요. (비밀번호 8자 이상)");
            req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
            return;
        }

        // 2) 중복 체크
        if (dao.existsByLoginId(userId) || dao.existsByEmail(email)) {
            req.setAttribute("error", "이미 사용 중인 아이디/이메일입니다.");
            req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
            return;
        }

        // 3) role 파라미터 반영 (화이트리스트)
        String roleParam = req.getParameter("role");
        String role = "USER";
        if ("ADMIN".equalsIgnoreCase(roleParam)) {
            role = "ADMIN";
        }

        // 4) 생년월일 파싱
        LocalDate birth = null;
        if (birthStr != null && !birthStr.isBlank()) {
            try {
                birth = LocalDate.parse(birthStr, DF);
            } catch (Exception e) {
                req.setAttribute("error", "생년월일 형식이 올바르지 않습니다. 예) 1995-01-01");
                req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
                return;
            }
        }

        // 5) DTO 채워서 저장
        UserDTO u = new UserDTO();
        u.setUserId(userId);
        u.setEmail(email);
        u.setPhone(phone);
        u.setBirth(birth);
        u.setGender(gender);
        u.setName(name);
        u.setNickname(nickname);
        u.setAddress(address);
        u.setRole(role);

        try {
            int newId = dao.create(u, pw);  // 💡 예외 발생 가능 구간

            if (newId <= 0) {
                req.setAttribute("error", "회원 가입에 실패했습니다.");
                req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("msg", "회원가입이 완료되었습니다. 로그인해주세요.");
            req.getRequestDispatcher("/user/login.jsp").forward(req, resp);

        } catch (RuntimeException e) {  // 💡 DAO에서 던진 RuntimeException 처리
            Throwable cause = e.getCause();  // 실제 SQLException 확인
            String msg = "서버 오류가 발생했습니다.";

            if (cause != null && cause.getMessage() != null && cause.getMessage().contains("Duplicate entry")) {
                if (cause.getMessage().contains("phone")) {
                    msg = "이미 등록된 전화번호입니다.";
                } else if (cause.getMessage().contains("email")) {
                    msg = "이미 등록된 이메일입니다.";
                } else if (cause.getMessage().contains("user_id")) {
                    msg = "이미 사용 중인 아이디입니다.";
                } else {
                    msg = "이미 등록된 정보가 있습니다.";
                }
            }

            req.setAttribute("error", msg);
            req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
        }
    }
}
