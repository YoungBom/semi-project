package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Pattern;

@WebServlet("/user/edit")
public class UserEditServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();
    private static final Pattern PHONE_PAT = Pattern.compile("^01[016789]-?\\d{3,4}-?\\d{4}$");

    // ✳️ 실제 JSP 위치: /user/edit.jsp  (만약 WEB-INF 아래면 "/WEB-INF/user/edit.jsp"로 바꾸세요)
    private static final String EDIT_JSP = "/user/edit.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute(SessionKeys.LOGIN_UID) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        long uid = (long) s.getAttribute(SessionKeys.LOGIN_UID);

        try {
            UserDTO me = userDao.findByPk(uid);
            req.setAttribute("me", me);
            req.getRequestDispatcher(EDIT_JSP).forward(req, resp);  // ✅ edit.jsp
        } catch (Exception e) {
            s.setAttribute("flash_error", "편집 화면을 여는 중 오류가 발생했습니다.");
            resp.sendRedirect(req.getContextPath() + "/mypage.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute(SessionKeys.LOGIN_UID) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        long uid = (long) s.getAttribute(SessionKeys.LOGIN_UID);

        String email    = req.getParameter("email");
        String nickname = req.getParameter("nickname");
        String phoneRaw = req.getParameter("phone");
        String birth    = req.getParameter("birth");
        String gender   = req.getParameter("gender");
        String address  = req.getParameter("address");

        if (phoneRaw == null || !PHONE_PAT.matcher(phoneRaw).matches()) {
            req.setAttribute("error_phone", "전화번호 형식이 올바르지 않습니다. 예) 010-1234-5678");
            keepForm(req, email, nickname, phoneRaw, birth, gender, address);
            req.getRequestDispatcher(EDIT_JSP).forward(req, resp);  // ✅ edit.jsp
            return;
        }

        String phoneNorm = normalizePhone(phoneRaw);

        try {
            if (userDao.existsByPhoneExceptUser(phoneNorm, uid)) {
                req.setAttribute("error_phone", "이미 등록된 전화번호입니다.");
                keepForm(req, email, nickname, phoneRaw, birth, gender, address);
                req.getRequestDispatcher(EDIT_JSP).forward(req, resp);  // ✅ edit.jsp
                return;
            }

            userDao.updateProfile(uid, email, nickname, phoneNorm, birth, gender, address);

            s.setAttribute("flash", "정보가 저장되었습니다.");
            resp.sendRedirect(req.getContextPath() + "/mypage.jsp");

        } catch (SQLIntegrityConstraintViolationException dup) {
            req.setAttribute("error_phone", "이미 등록된 전화번호입니다.");
            keepForm(req, email, nickname, phoneRaw, birth, gender, address);
            req.getRequestDispatcher(EDIT_JSP).forward(req, resp);  // ✅ edit.jsp

        } catch (SQLException e) {
            req.setAttribute("error_phone", "전화번호 저장 중 오류가 발생했습니다.");
            keepForm(req, email, nickname, phoneRaw, birth, gender, address);
            req.getRequestDispatcher(EDIT_JSP).forward(req, resp);  // ✅ edit.jsp
        }
    }

    private String normalizePhone(String s) {
        return s == null ? null : s.replaceAll("[^0-9]", "");
    }

    private void keepForm(HttpServletRequest req, String email, String nickname, String phone,
                          String birth, String gender, String address) {
        req.setAttribute("form_email", email);
        req.setAttribute("form_nickname", nickname);
        req.setAttribute("form_phone", phone);   // 화면엔 원래 포맷 유지
        req.setAttribute("form_birth", birth);
        req.setAttribute("form_gender", gender);
        req.setAttribute("form_address", address);
    }
}
