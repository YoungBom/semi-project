package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@WebServlet("/user/edit")
public class UserEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO userDao = new UserDAO();
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Integer uid = (s != null) ? (Integer) s.getAttribute(SessionKeys.LOGIN_UID) : null;
        if (uid == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Optional<UserDTO> opt = userDao.findById(uid);
        if (opt.isEmpty()) {
            // 세션은 있는데 DB에 사용자가 없으면 로그인부터 다시
            s.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("user", opt.get());

        // ★ JSP 파일로 forward (서블릿 매핑으로 보내지 마세요!)
        req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
        // 보호 경로를 쓰는 경우: "/WEB-INF/views/user/edit.jsp"
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession s = req.getSession(false);
        Integer uid = (s != null) ? (Integer) s.getAttribute(SessionKeys.LOGIN_UID) : null;
        if (uid == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String email    = Optional.ofNullable(req.getParameter("email")).orElse("").trim();
        String phone    = Optional.ofNullable(req.getParameter("phone")).orElse("").trim();
        String birthStr = req.getParameter("birth");
        String gender   = Optional.ofNullable(req.getParameter("gender")).orElse("선택안함").trim();
        String name     = Optional.ofNullable(req.getParameter("name")).orElse("").trim();
        String nickname = Optional.ofNullable(req.getParameter("nickname")).orElse("").trim();
        String address  = Optional.ofNullable(req.getParameter("address")).orElse("").trim();

        // ===== 유효성 검사 =====
        // 1. 이메일 형식 검사
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            req.setAttribute("error", "올바른 이메일 형식이 아닙니다.");
            redisplay(req, resp, uid);
            return;
        }

        // 2. 전화번호 형식 검사
        if (!phone.isEmpty() && !phone.matches("^01[0-9]{8,9}$")) {
            req.setAttribute("error", "휴대폰 번호 형식이 올바르지 않습니다. (01012345678)");
            redisplay(req, resp, uid);
            return;
        }

        // 3. 생년월일 검사 (미래 날짜 불가)
        String birth = null;
        if (birthStr != null && !birthStr.isBlank()) {
            try {
                LocalDate date = LocalDate.parse(birthStr, DF);
                if (date.isAfter(LocalDate.now())) {
                    req.setAttribute("error", "생년월일은 미래 날짜로 지정할 수 없습니다.");
                    redisplay(req, resp, uid);
                    return;
                }
                birth = birthStr;
            } catch (Exception e) {
                req.setAttribute("error", "생년월일 형식이 올바르지 않습니다.");
                redisplay(req, resp, uid);
                return;
            }
        }

        // 4. 성별 검사 (허용된 값만)
        if (!gender.equals("남") && !gender.equals("여") && !gender.equals("선택안함")) {
            gender = "선택안함";
        }

        // ===== DB 업데이트 시도 =====
        try {
            boolean ok = userDao.updateProfile(uid, email, phone, birth, gender, name, nickname, address);
            if (!ok) {
                req.setAttribute("error", "수정에 실패했습니다. 잠시 후 다시 시도해주세요.");
                redisplay(req, resp, uid);
                return;
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("Duplicate")) {
                if (msg.contains("phone")) {
                    req.setAttribute("error", "이미 사용 중인 전화번호입니다.");
                } else if (msg.contains("email")) {
                    req.setAttribute("error", "이미 사용 중인 이메일입니다.");
                } else {
                    req.setAttribute("error", "중복된 정보가 있습니다.");
                }
            } else {
                req.setAttribute("error", "수정 중 오류가 발생했습니다.");
            }
            redisplay(req, resp, uid);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/user/mypage");
    }

    // 재사용용 편의 메서드
    private void redisplay(HttpServletRequest req, HttpServletResponse resp, Integer uid)
            throws ServletException, IOException {
        userDao.findById(uid).ifPresent(u -> req.setAttribute("user", u));
        req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
    }

}
