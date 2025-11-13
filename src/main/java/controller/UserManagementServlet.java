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


@WebServlet("/user/management")
public class UserManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String keyword = request.getParameter("keyword");
		UserDAO userDao = new UserDAO();
		List<UserDTO> userList = new ArrayList<UserDTO>();
		
		
		if(keyword == null || keyword.trim().isEmpty()) {
			userList = userDao.getAllUserList();
		} else {
			keyword = keyword.trim();
			userList = userDao.searchUser(keyword);
		}
		
		
		
		request.setAttribute("userList", userList);
		request.getRequestDispatcher("/user/userManagement.jsp").forward(request, response);
		
	}

}
