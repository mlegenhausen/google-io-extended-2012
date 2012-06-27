package org.gdg.ioext12;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class DeleteNoteServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1851946003715511015L;

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String id = req.getRequestURI();
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		if (user == null){
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		} else {
			if (id != null){
				//final NodeDAO dao = 
			}
		}
		
		
	}


	
	
	

}
