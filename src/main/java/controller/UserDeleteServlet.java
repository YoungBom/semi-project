package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import dao.UserDAO;
import dto.UserDTO;

/**
 * Servlet implementation class UserDeleteServlet
 */
@WebServlet("/user/delete")
public class UserDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final UserDAO dao = new UserDAO();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserDeleteServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	        HttpSession session = request.getSession(false);

	        if (session == null || session.getAttribute("LOGIN_USER") == null) {
	        	response.sendRedirect(request.getContextPath() + "/login.jsp");
	            return;
	        }

	        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
	        String inputPw = request.getParameter("password");

	        try {
	            boolean isValid = dao.checkPassword(user.getUserId(), inputPw);

	            if (!isValid) {
	            	response.setContentType("text/plain; charset=UTF-8");
	            	response.getWriter().write("INVALID");
	                return;
	            }

	            boolean deleted = dao.deleteUser(user.getUserId());
	            if (deleted) {
	                session.invalidate(); // 로그아웃
	                response.setContentType("text/plain; charset=UTF-8");
	                response.getWriter().write("SUCCESS");
	            } else {
	            	response.setContentType("text/plain; charset=UTF-8");
	            	response.getWriter().write("FAIL");
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            response.setContentType("text/plain; charset=UTF-8");
	            response.getWriter().write("ERROR");
	        }
	    }

}
