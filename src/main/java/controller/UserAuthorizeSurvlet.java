package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.UserDAO;

@WebServlet("/user/authorize")
public class UserAuthorizeSurvlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		
		UserDAO userDao = new UserDAO();
		int result = userDao.setAuthorizeAdmin(id);
		
		if(result > 0) {
			response.sendRedirect(request.getContextPath() + "/user/management");
		} else {
			request.setAttribute("error", "삭제 실패");
			request.getRequestDispatcher("/user/userManagement.jsp").forward(request, response);			
		}
		
	}



}
