package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import dao.BurgerDAO;
import dto.BurgerDTO;
import dto.BurgerDetailsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/burger/add")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 1,   // 1MB 이상이면 임시파일에 저장
		maxFileSize = 1024 * 1024 * 10,        // 단일 파일 최대 10MB
		maxRequestSize = 1024 * 1024 * 50      // 전체 요청 최대 50MB
)
public class BurgerAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();

		try {
			// 🧩 1️⃣ 기본 파라미터 수집
			String name = req.getParameter("name");
			String brand = req.getParameter("brand");
			int price = Integer.parseInt(req.getParameter("price"));
			String pattyType = req.getParameter("pattyType");
			
			int calories = Integer.parseInt(req.getParameter("calories"));
			int carbohydrates = Integer.parseInt(req.getParameter("carbohydrates"));
			int protein = Integer.parseInt(req.getParameter("protein"));
			int fat = Integer.parseInt(req.getParameter("fat"));
			int sodium = Integer.parseInt(req.getParameter("sodium"));
			int sugar = Integer.parseInt(req.getParameter("sugar"));
			String allergyInfo = req.getParameter("allergyInfo");
			
			Part filePart = req.getPart("imagePath");
			String fileName = null;
			String imageUrl = null;

			if (filePart != null && filePart.getSize() > 0) {
				fileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();

				String uploadPath = req.getServletContext().getRealPath("/image");
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) uploadDir.mkdirs();

				filePart.write(uploadPath + File.separator + fileName);

				imageUrl = "/image/" + fileName;
			}

			BurgerDTO burger = new BurgerDTO(name, price, imageUrl, brand, pattyType);
			BurgerDetailsDTO burgerDetails = new BurgerDetailsDTO(
					calories, carbohydrates, protein, fat, sodium, sugar, allergyInfo
			);
			
			BurgerDAO dao = new BurgerDAO();
			int result = dao.insertBurger(burger, burgerDetails);

			if (result > 0) {
				out.println("<script>");
				out.println("alert('버거 등록이 완료되었습니다!');");
				out.println("location.href='" + req.getContextPath() + "/main.jsp';");
				out.println("</script>");
			} else {
				out.println("<script>");
				out.println("alert('버거 등록에 실패했습니다. 다시 시도해주세요.');");
				out.println("history.back();");
				out.println("</script>");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("❌ 예외 발생");

			out.println("<script>");
			out.println("alert('예외 발생: " + e.getMessage().replace("'", "\\'") + "');");
			out.println("history.back();");
			out.println("</script>");
		}
	}
}
