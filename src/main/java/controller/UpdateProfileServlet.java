package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.PhoneUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;

@WebServlet("/user/profile/update")
public class UpdateProfileServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession s = req.getSession(false);
        Integer uid = (s != null) ? (Integer) s.getAttribute("LOGIN_UID") : null;
        if (uid == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        String email    = req.getParameter("email");
        String phoneRaw = req.getParameter("phone");
        String nickname = req.getParameter("nickname");
        String address  = req.getParameter("address");

        // 값 보존
        req.setAttribute("email", email);
        req.setAttribute("phone", phoneRaw);
        req.setAttribute("nickname", nickname);
        req.setAttribute("address", address);

        try {
            String phone = PhoneUtil.normalize(phoneRaw);
            if (phone == null) {
                req.setAttribute("error", "전화번호 형식이 올바르지 않습니다. 예) 010-1234-5678");
                req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
                return;
            }

            if (userDao.existsByPhone(phone, uid)) {
                req.setAttribute("error", "이미 사용 중인 전화번호입니다.");
                req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
                return;
            }

            UserDTO u = new UserDTO();
            u.setId(uid);
            u.setEmail(email);
            u.setPhone(phone);
            u.setNickname(nickname);
            u.setAddress(address);

            try {
                userDao.updateProfile(u);
            } catch (SQLIntegrityConstraintViolationException ex) {
                req.setAttribute("error", "이미 사용 중인 전화번호 또는 이메일입니다.");
                req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
                return;
            }

            s.setAttribute("FLASH_SUCCESS", "프로필이 수정되었습니다.");
            resp.sendRedirect(req.getContextPath() + "/user/profile");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
