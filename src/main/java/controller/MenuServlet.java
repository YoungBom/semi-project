package controller;

import java.awt.print.Printable;
import java.io.IOException;
import java.util.List;

import dao.BurgerDAO;
import dto.BurgerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/burger/menu")
public class MenuServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BurgerDAO dao = new BurgerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<BurgerDTO> list;
        // 🔍 검색어가 있으면 검색 결과, 없으면 전체 목록
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = dao.searchBurgers(keyword);
        } else {
            list = dao.getAllBurgers();
        }

        req.setAttribute("burgerList", list);
        req.setAttribute("keyword", keyword);

        // ✅ JSP로 포워딩
        req.getRequestDispatcher("/menu.jsp").forward(req, resp);
    }
}
