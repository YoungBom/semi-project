package controller;

import dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/id/lookup")
public class IdLookupServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name  = req.getParameter("name");
        String email = req.getParameter("email");

        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            req.setAttribute("error", "이름과 이메일을 입력하세요.");
            req.getRequestDispatcher("/user/id_lookup.jsp").forward(req, resp);
            return;
        }

        Optional<String> loginId = userDao.findLoginIdByNameAndEmail(name.trim(), email.trim());
        if (loginId.isPresent()) {
            req.setAttribute("found_user_id", loginId.get());
        } else {
            req.setAttribute("error", "일치하는 사용자를 찾을 수 없습니다.");
        }
        req.getRequestDispatcher("/user/id_lookup.jsp").forward(req, resp);
    }
}
