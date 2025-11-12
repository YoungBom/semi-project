package controller;

import java.io.IOException;
import java.util.List;

import dao.BoardDAO;
import dto.BoardDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/board/list")
public class BoardListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String category = req.getParameter("category");
        String type = req.getParameter("type");
        String keyword = req.getParameter("keyword");

        int page = 1;
        int limit = 20;
        if (req.getParameter("page") != null) {
            try {
                page = Integer.parseInt(req.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        BoardDAO dao = new BoardDAO();

        int totalCount = dao.getTotalCount(category, type, keyword);

        List<BoardDTO> boardList = dao.getBoardList(category, type, keyword, page, limit);

        int totalPage = (int) Math.ceil((double) totalCount / limit);

        req.setAttribute("boardList", boardList);
        req.setAttribute("selectedCategory", category == null ? "전체" : category);
        req.setAttribute("type", type);
        req.setAttribute("keyword", keyword);
        req.setAttribute("page", page);
        req.setAttribute("totalPage", totalPage);

        req.getRequestDispatcher("/boardList.jsp").forward(req, resp);
    }
}
