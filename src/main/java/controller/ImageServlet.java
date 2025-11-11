package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet({"/image/*", "/profile/*"})
public class ImageServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "d:\\upload";
    private static final String PROFILE_DIR = "d:\\profile";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String servletPath = req.getServletPath(); // /image or /profile
        String fileName = req.getPathInfo().substring(1);

        File file;
        if (servletPath.startsWith("/profile")) {
            file = new File(PROFILE_DIR, fileName);
        } else {
            file = new File(UPLOAD_DIR, fileName);
        }

        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(file.getName());
        if (mime == null) mime = "application/octet-stream";
        resp.setContentType(mime);

        try (InputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        }
    }
}
