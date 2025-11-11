package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/user/uploadProfile")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB 제한
public class ProfileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("LOGIN_USERID");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Part filePart = req.getPart("profileImage");
        if (filePart == null || filePart.getSize() == 0) {
            resp.sendRedirect(req.getContextPath() + "/user/mypage.jsp?error=noFile");
            return;
        }

        // D드라이브 경로
        String uploadDir = "D:\\profile";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // 파일 이름 지정
        String originalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = userId + "_" + time + "_" + originalName;

        // 저장
        File file = new File(dir, fileName);
        filePart.write(file.getAbsolutePath());

        // DB 저장용 경로
        String dbPath = "/profile/" + fileName;

        dao.updateProfileImg(userId, dbPath);

        // 성공 후 마이페이지로 리다이렉트
        resp.sendRedirect(req.getContextPath() + "/user/mypage.jsp?success=1");
    }
    
    
}
