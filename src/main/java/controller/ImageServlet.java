package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.*;
/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {
	 	private static final String UPLOAD_DIR = "d:\\upload";

	    @Override
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws ServletException, IOException {

	        String fileName = req.getPathInfo().substring(1); // "/abc.png" → "abc.png"
	        File file = new File(UPLOAD_DIR, fileName);

	        if (!file.exists()) {
	            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	            return;
	        }

	        // MIME 타입 자동 설정
	        String mime = getServletContext().getMimeType(file.getName());
	        if (mime == null) mime = "application/octet-stream";
	        resp.setContentType(mime);

	        // 파일 스트리밍
	        try (FileInputStream fis = new FileInputStream(file);
	             OutputStream os = resp.getOutputStream()) {
	            byte[] buffer = new byte[8192];
	            int bytesRead;
	            while ((bytesRead = fis.read(buffer)) != -1) {
	                os.write(buffer, 0, bytesRead);
	            }
	        }
	    }
}
