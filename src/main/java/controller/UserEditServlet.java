package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/edit")
public class UserEditServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute(SessionKeys.LOGIN_UID) == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
            return;
        }
        long uid = (long) s.getAttribute(SessionKeys.LOGIN_UID);

        try {
            UserDTO me = userDao.findByPk(uid);
            if (me == null) {
                s.invalidate();
                resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
                return;
            }
            req.setAttribute("me", me);
            req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute(SessionKeys.LOGIN_UID) == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
            return;
        }
        long uid = (long) s.getAttribute(SessionKeys.LOGIN_UID);

        String email   = req.getParameter("email");
        String nickname= req.getParameter("nickname");
        String phone   = req.getParameter("phone");
        String birth   = req.getParameter("birth");
        String gender  = req.getParameter("gender");
        String address = req.getParameter("address");

        try {
            int rows = userDao.updateProfile(uid, email, nickname, phone, birth, gender, address);
            if (rows == 1) {
                resp.sendRedirect(req.getContextPath() + "/user/mypage?updated=1");
                return;
            }
            req.setAttribute("error", "수정에 실패했습니다.");
            UserDTO me = userDao.findByPk(uid);
            req.setAttribute("me", me);
            req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
