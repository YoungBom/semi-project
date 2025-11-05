package controller;

import dao.UserDAO;                    // ✅ 실제 파일명/클래스명과 일치
import model.User;
import util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/register")
public class SignupServlet extends HttpServlet {
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userId   = req.getParameter("user_id");
        String pw       = req.getParameter("user_pw");
        String pw2      = req.getParameter("user_pw_confirm");
        String email    = req.getParameter("email");
        String name     = req.getParameter("name");
        String gender   = req.getParameter("gender");
        String birth    = req.getParameter("birth");
        String phone    = req.getParameter("phone");
        String nickname = req.getParameter("nickname");
        String address  = req.getParameter("address");

        // 비번 확인
        if (pw == null || !pw.equals(pw2)) {
            req.setAttribute("error", "비밀번호가 일치하지 않습니다.");
            keepForm(req, userId, email, name, gender, birth, phone, nickname, address);
            req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
            return;
        }

        try {
            // 아이디/이메일 중복 체크
            if (dao.findByLoginId(userId) != null) {
                req.setAttribute("error", "이미 사용 중인 아이디입니다.");
                keepForm(req, userId, email, name, gender, birth, phone, nickname, address);
                req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
                return;
            }
            if (dao.findByEmail(email) != null) {
                req.setAttribute("error", "이미 사용 중인 이메일입니다.");
                keepForm(req, userId, email, name, gender, birth, phone, nickname, address);
                req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
                return;
            }

            // 사용자 생성 및 저장
            User u = new User();
            u.setUser_id(userId);
            u.setUser_pw(PasswordUtil.hash(pw));   // 해시 저장
            u.setEmail(email);
            u.setName(name);
            u.setGender(gender);
            u.setBirth(birth);
            u.setPhone(phone);
            u.setNickname(nickname);
            u.setAddress(address);

            dao.insert(u);

            // 가입 성공 → 로그인 페이지로 (서블릿 매핑이 /login이면 그걸로 변경)
            resp.sendRedirect(req.getContextPath() + "/user/login.jsp?joined=1");

        } catch (SQLIntegrityConstraintViolationException dup) {
            // DB 유니크 제약 위반(동시요청 대비)
            String msg = dup.getMessage();
            if (msg != null && msg.toLowerCase().contains("user_id")) {
                req.setAttribute("error", "이미 사용 중인 아이디입니다.");
            } else if (msg != null && msg.toLowerCase().contains("email")) {
                req.setAttribute("error", "이미 사용 중인 이메일입니다.");
            } else {
                req.setAttribute("error", "중복된 정보가 있습니다. 입력값을 확인해주세요.");
            }
            keepForm(req, userId, email, name, gender, birth, phone, nickname, address);
            req.getRequestDispatcher("/user/register.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace(); // 서버 로그
            req.setAttribute("error", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            keepForm(req, userId, email, name, gender, birth, phone, nickname, address);
            req.getRequestDispatcher("/user/register.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private static void keepForm(HttpServletRequest req, String userId, String email, String name,
                                 String gender, String birth, String phone, String nickname, String address) {
        req.setAttribute("form_user_id", userId);
        req.setAttribute("form_email", email);
        req.setAttribute("form_name", name);
        req.setAttribute("form_gender", gender);
        req.setAttribute("form_birth", birth);
        req.setAttribute("form_phone", phone);
        req.setAttribute("form_nickname", nickname);
        req.setAttribute("form_address", address);
    }
}
