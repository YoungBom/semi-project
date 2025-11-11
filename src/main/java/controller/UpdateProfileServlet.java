package controller;

import dao.UserDAO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/user/updateProfile")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10) // 10MB
public class UpdateProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String PROFILE_DIR = "d:\\profile";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("LOGIN_USERID");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
            return;
        }

        Part part = req.getPart("profileImg");
        String fileName = null;

        if (part != null && part.getSize() > 0) {
            // 확장자 추출
            String submittedName = part.getSubmittedFileName();
            String ext = submittedName.substring(submittedName.lastIndexOf("."));
            fileName = UUID.randomUUID().toString() + ext;

            File dir = new File(PROFILE_DIR);
            if (!dir.exists()) dir.mkdirs();

            part.write(new File(dir, fileName).getAbsolutePath());
        }

        if (fileName != null) {
            UserDAO dao = new UserDAO();
            dao.updateProfileImg(userId, fileName);

            // ✅ 세션 갱신
            UserDTO updatedUser = dao.findByUserId(userId);
            if (updatedUser != null) {
                session.setAttribute("LOGIN_USER", updatedUser);
            }
        }

        // ✅ 리다이렉트 (세션 기반이라 괜찮음)
        resp.sendRedirect(req.getContextPath() + "/user/mypage");
    }
}
