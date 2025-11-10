package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@WebServlet("/user/edit")
public class UserEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO userDao = new UserDAO();
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Integer uid = (s != null) ? (Integer) s.getAttribute(SessionKeys.LOGIN_UID) : null;
        if (uid == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Optional<UserDTO> opt = userDao.findById(uid);
        if (opt.isEmpty()) {
            // 세션은 있는데 DB에 사용자가 없으면 로그인부터 다시
            s.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("user", opt.get());

        // ★ JSP 파일로 forward (서블릿 매핑으로 보내지 마세요!)
        req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
        // 보호 경로를 쓰는 경우: "/WEB-INF/views/user/edit.jsp"
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession s = req.getSession(false);
        Integer uid = (s != null) ? (Integer) s.getAttribute(SessionKeys.LOGIN_UID) : null;
        if (uid == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String email   = req.getParameter("email");
        String phone   = req.getParameter("phone");
        String birth   = req.getParameter("birth"); // yyyy-MM-dd 또는 빈값
        String gender  = req.getParameter("gender");
        String name    = req.getParameter("name");
        String nickname= req.getParameter("nickname");
        String address = req.getParameter("address");

        // birth 값 간단 검증(비어있으면 null 저장)
        if (birth != null && !birth.isBlank()) {
            try { LocalDate.parse(birth, DF); } catch (Exception ignore) { birth = null; }
        } else {
            birth = null;
        }

        boolean ok = userDao.updateProfile(uid, email, phone, birth, gender, name, nickname, address);
        if (!ok) {
            req.setAttribute("error", "수정에 실패했습니다. 잠시 후 다시 시도해주세요.");
            // 다시 편집 화면으로
            Optional<UserDTO> opt = userDao.findById(uid);
            opt.ifPresent(u -> req.setAttribute("user", u));
            req.getRequestDispatcher("/user/edit.jsp").forward(req, resp);
            return;
        }

        // 성공 시 마이페이지로 이동
        resp.sendRedirect(req.getContextPath() + "/user/mypage");
    }
}
