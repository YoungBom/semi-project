package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
        // id 파라미터 유효성 검사
        String idParam = req.getParameter("id");
        if (idParam == null || !idParam.matches("\\d+")) {
            // 숫자가 아니거나 아예 없는 경우 메인으로 리다이렉트
            resp.sendRedirect(req.getContextPath() + "/main");
            return;
        }
        
        int id = Integer.parseInt(idParam);
        
        
        // burgerDAO에서 버거 정보 가져오기
        BurgerDTO burger = burgerDAO.getBurgerById(id);

        // 존재하지 않는 버거 ID일 경우 main 페이지로 이동
        if (burger == null) {
            resp.sendRedirect(req.getContextPath() + "/main");
            return;
        }
        req.setAttribute("burger", burger);
        
        // 로그인시 사용자 닉네임 정보 가져오기
        HttpSession us = req.getSession();
        String nickname = (String) us.getAttribute("LOGIN_NAME");
        req.setAttribute("nickname", nickname);
        
        
        // 리뷰 목록 불러오기
        ReviewDAO reviewDAO = new ReviewDAO();
        List<ReviewDTO> reviewList = reviewDAO.getReview(id);
        req.setAttribute("reviewList", reviewList);
        
        req.getRequestDispatcher("/burgerDetails.jsp?id="+id).forward(req, resp);
    }
}
