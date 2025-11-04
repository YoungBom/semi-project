package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.BurgerDAO;
import dto.BurgerDTO;

@WebServlet("/burger/details")
public class BurgerDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BurgerDAO burgerDAO = new BurgerDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	int id = Integer.parseInt(req.getParameter("id"));
        BurgerDTO burger = burgerDAO.getBurgerById(id);

        req.setAttribute("burger", burger);
        req.getRequestDispatcher("/burgerDetails.jsp").forward(req, resp);
    }
}
