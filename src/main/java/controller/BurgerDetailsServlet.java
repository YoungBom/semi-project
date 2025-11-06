package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.BurgerDAO;
import dao.ReviewDAO;
import dto.BurgerDTO;
import dto.ReviewDTO;

@WebServlet("/burger/details")
public class BurgerDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BurgerDAO burgerDAO = new BurgerDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	int id = Integer.parseInt(req.getParameter("id"));
    	int userId = 1;
        BurgerDTO burger = burgerDAO.getBurgerById(id);

        req.setAttribute("burger", burger);
        
//      리뷰 목록 불러오기
        ReviewDAO reviewDAO = new ReviewDAO();
        List<ReviewDTO> reviewList = reviewDAO.getReview(id);
        req.setAttribute("reviewList", reviewList);
        
        req.getRequestDispatcher("/burgerDetails.jsp?id="+id).forward(req, resp);
    }
}
