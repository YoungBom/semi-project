package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/user/mypage") // ✅ 이 매핑만 사용
public class MyPageServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession s = req.getSession(false);
        Object uidObj = (s != null) ? s.getAttribute(SessionKeys.LOGIN_UID) : null;
        if (uidObj == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int uid = (uidObj instanceof Integer) ? (Integer) uidObj : Integer.parseInt(uidObj.toString());
        Optional<UserDTO> opt = userDao.findById(uid);
        if (opt.isEmpty()) {
            req.setAttribute("error", "사용자 정보를 찾을 수 없습니다.");
            req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("user", opt.get());
        req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
    }
}
