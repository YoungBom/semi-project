package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.PasswordUtil;
import util.SessionKeys;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/user/password_change", "/user/password-change" })
public class PasswordChangeServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute(SessionKeys.LOGIN_UID) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        long uid = (long) s.getAttribute(SessionKeys.LOGIN_UID);

        String currentPw = req.getParameter("current_pw");
        String newPw     = req.getParameter("new_pw");

        // 입력값 검사
        if (currentPw == null || currentPw.isBlank() || newPw == null || newPw.isBlank()) {
            s.setAttribute("flash_error", "비밀번호를 모두 입력해 주세요.");
            resp.sendRedirect(req.getContextPath() + "/user/password_change.jsp");
            return;
        }
        if (newPw.length() < 8) {
            s.setAttribute("flash_error", "새 비밀번호는 8자 이상이어야 합니다.");
            resp.sendRedirect(req.getContextPath() + "/user/password_change.jsp");
            return;
        }

        try {
            UserDTO me = userDao.findByPk(uid);
            if (me == null) {
                s.setAttribute("flash_error", "사용자 정보를 찾을 수 없습니다.");
                resp.sendRedirect(req.getContextPath() + "/user/password_change.jsp");
                return;
            }

            // 현재 비밀번호 검증
            if (!PasswordUtil.verify(currentPw, me.getPasswordHash())) {
                s.setAttribute("flash_error", "현재 비밀번호가 일치하지 않습니다.");
                resp.sendRedirect(req.getContextPath() + "/user/password_change.jsp");
                return;
            }

            // 새 비밀번호가 현재와 동일한지 방지(선택)
            if (PasswordUtil.verify(newPw, me.getPasswordHash())) {
                s.setAttribute("flash_error", "새 비밀번호가 현재 비밀번호와 동일합니다.");
                resp.sendRedirect(req.getContextPath() + "/user/password_change.jsp");
                return;
            }

            // 변경
            String newHash = PasswordUtil.hash(newPw);
            userDao.updatePassword(uid, newHash);

            s.setAttribute("flash", "비밀번호가 변경되었습니다.");
            resp.sendRedirect(req.getContextPath() + "/main.jsp");
        } catch (SQLException e) {
            s.setAttribute("flash_error", "처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
            resp.sendRedirect(req.getContextPath() + "/user/password_change.jsp");
        }
    }

    // GET 접근 시 폼으로 안내(선택)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/user/password_change.jsp");
    }
}
