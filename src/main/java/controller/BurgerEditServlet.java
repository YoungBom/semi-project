package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Base64;

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

@WebServlet("/burger/edit")
@MultipartConfig
public class BurgerEditServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private BurgerDAO burgerDAO = new BurgerDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		BurgerDTO burger = burgerDAO.getBurgerById(id);

		req.setAttribute("burger", burger);
		req.getRequestDispatcher("/editBurger.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		
		int id = Integer.parseInt(req.getParameter("id"));
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
		String[] allergyArray = req.getParameterValues("allergyInfo");
		String allergyInfo = String.join(", ", allergyArray);
		if (allergyArray != null) allergyInfo = String.join(", ", allergyArray);
		
		Part filePart = req.getPart("imagePath");
		String image = null;
		
		if (filePart != null && filePart.getSize() > 0) {
			try (InputStream inputStream = filePart.getInputStream()) {
			    byte[] imageBytes = inputStream.readAllBytes();
			    image = Base64.getEncoder().encodeToString(imageBytes);
			}
		} else {
			image = burgerDAO.getBurgerById(id).getImagePath();
		}
		
        BurgerDTO burger = new BurgerDTO(name, price, image, brand, pattyType);
        burger.setId(id);

        BurgerDetailsDTO details = new BurgerDetailsDTO(
                calories, carbohydrates, protein, fat, sodium, sugar, allergyInfo);

        int result = burgerDAO.updateBurger(burger, details);

		if (result > 0) {
			out.println("<script>");
			out.println("alert('버거 수정이 완료되었습니다!');");
			out.println("location.href='" + req.getContextPath() + "/burger/edit?id=" + burger.getId() + "';");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('버거 수정에 실패했습니다. 다시 시도해주세요.');");
			out.println("history.back();");
			out.println("</script>");
		}
	}
}
