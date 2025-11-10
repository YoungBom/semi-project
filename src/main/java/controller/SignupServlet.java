package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/signup")
public class SignupServlet extends HttpServlet {
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userId  = req.getParameter("user_id");
        String pw      = req.getParameter("user_pw");
        String email   = req.getParameter("email");
        String name    = req.getParameter("name");
        String gender  = req.getParameter("gender");
        String birth   = req.getParameter("birth");
        String phone   = req.getParameter("phone");
        String nickname= req.getParameter("nickname");
        String address = req.getParameter("address");

        try {
            if (dao.existsByEmail(email)) {
                req.setAttribute("error", "이미 사용 중인 이메일입니다.");
                req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
                return;
            }

            UserDTO u = new UserDTO();
            u.setUserId(userId);
            u.setPasswordHash(PasswordUtil.hash(pw));
            u.setEmail(email);
            u.setName(name);
            u.setGender(gender);
            u.setBirth(birth);
            u.setPhone(phone);
            u.setNickname(nickname);
            u.setAddress(address);
            u.setRole("USER");

            long newId = dao.create(u);
            if (newId <= 0) { 
                req.setAttribute("error", "회원 가입에 실패했습니다.");
                req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
