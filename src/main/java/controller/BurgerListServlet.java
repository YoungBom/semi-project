package controller;

import java.io.IOException;
import java.util.List;

import dao.BurgerDAO;
import dto.BurgerDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/burger/list")
public class BurgerListServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BurgerDAO dao = new BurgerDAO();
        List<BurgerDTO> burgerList = dao.getAllBurgers();

        req.setAttribute("burgerList", burgerList);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/listBurger.jsp");
        dispatcher.forward(req, resp);
	}
}
