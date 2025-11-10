package controller;

import dao.UserDAO;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/update")
public class UpdateProfileServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession s = req.getSession(false);
        Object uidObj = (s != null) ? s.getAttribute(SessionKeys.LOGIN_UID) : null;
        if (uidObj == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int uid = (uidObj instanceof Integer) ? (Integer) uidObj : Integer.parseInt(uidObj.toString());

        String email   = req.getParameter("email");
        String phone   = req.getParameter("phone");
        String birth   = req.getParameter("birth");   // 현재 DB가 VARCHAR(255)라 문자열로 유지
        String gender  = req.getParameter("gender");
        String name    = req.getParameter("name");
        String nickname= req.getParameter("nickname");
        String address = req.getParameter("address");

        boolean ok = userDao.updateProfile(uid, email, phone, birth, gender, name, nickname, address);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/user/mypage?msg=updated");
        } else {
            req.setAttribute("error", "수정에 실패했습니다.");
            req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
        }
    }
}
