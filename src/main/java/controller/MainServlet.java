package controller;

import java.io.IOException;
import java.util.List;

import dao.BurgerDAO;
import dto.BurgerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BurgerDAO burgerDAO = new BurgerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<BurgerDTO> burgerList = burgerDAO.getAllBurgers();

        req.setAttribute("burgerList", burgerList);
        req.getRequestDispatcher("/main.jsp").forward(req, resp);
    }
}
