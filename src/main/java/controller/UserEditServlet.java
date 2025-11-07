package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/user/edit")  // ★ 마이페이지의 '정보 수정'이 여기로 옴
public class UserEditServlet extends HttpServlet {
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
            req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("user", opt.get());
        req.getRequestDispatcher("/user/edit.jsp").forward(req, resp); // ★ 실제 존재하는 파일명으로
    }
}
