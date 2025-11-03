package controller;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.BurgerDetailsDAO;
import dto.BurgerDTO;
import dto.BurgerDetailsDTO;

@WebServlet("/burgerDetails")
public class BurgerDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1️⃣ URL에서 버거 ID 파라미터 읽기 (예: ?id=3)
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("main.jsp");
            return;
        }

        int burgerId = Integer.parseInt(idParam);

        // 2️⃣ DAO 통해 DB 조회
        BurgerDetailsDAO burgerDAO = new BurgerDetailsDAO();
        BurgerDTO burger = burgerDAO.getBurgerById(burgerId);
        BurgerDetailsDTO details = burgerDAO.getBurgerDetailsById(burgerId);

        // 3️⃣ JSP에 전달
        request.setAttribute("burger", burger);
        request.setAttribute("details", details);

        // 4️⃣ JSP로 포워딩
        RequestDispatcher rd = request.getRequestDispatcher("/burgerDetails.jsp");
        rd.forward(request, response);
    }
}
