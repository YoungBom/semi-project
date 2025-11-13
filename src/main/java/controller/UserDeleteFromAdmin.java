package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.UserDAO;
import dto.UserDTO;

@WebServlet("/user/deletefromadmin")
public class UserDeleteFromAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		
		UserDAO userDao = new UserDAO();
		Boolean result = userDao.deleteUserById(userId);
		
		if (result) {			
			response.sendRedirect(request.getContextPath() + "/user/management");
		} else {
			request.setAttribute("error", "삭제 실패");
			request.getRequestDispatcher("/user/userManagement.jsp").forward(request, response);			
		}
	}

}
