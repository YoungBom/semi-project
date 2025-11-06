package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.BurgerDAO;
import dto.BurgerDTO;

/**
 * Servlet implementation class FilterServlet
 */
@WebServlet("/filter")
public class FilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		
		String patty = request.getParameter("patty");
		BurgerDAO dao = new BurgerDAO();
		List<BurgerDTO> burgers;
		
		if (patty == null || patty.equals("all")) {
			burgers = dao.getAllBurgers();
		} else {
			burgers = dao.getBurgerByPatty(patty);
		}
		
		Gson gson = new GsonBuilder()
		        .serializeNulls() 
		        .create();
		String json = gson.toJson(burgers);
		response.getWriter().write(json);
		
	}

}
