package controller;

import dao.UserDao;
import model.User;
import util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/register")
public class SignupServlet extends HttpServlet {
    private final UserDao dao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userId  = req.getParameter("user_id");
        String pw      = req.getParameter("user_pw");
        String pw2     = req.getParameter("user_pw_confirm");
        String email   = req.getParameter("email");
        String name    = req.getParameter("name");
        String gender  = req.getParameter("gender");
        String birth   = req.getParameter("birth");
        String phone   = req.getParameter("phone");
        String nickname= req.getParameter("nickname");
        String address = req.getParameter("address");

        if (pw == null || !pw.equals(pw2)) {
            req.setAttribute("error", "비밀번호가 일치하지 않습니다.");
            req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
            return;
        }

        try {
            if (dao.idExists(userId)) {
                req.setAttribute("error", "이미 사용중인 아이디입니다.");
                req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
                return;
            }
            if (dao.emailExists(email)) {
                req.setAttribute("error", "이미 사용중인 이메일입니다.");
                req.getRequestDispatcher("/user/register.jsp").forward(req, resp);
                return;
            }

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
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
