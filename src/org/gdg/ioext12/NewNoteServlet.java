package org.gdg.ioext12;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gdg.ioext12.data.Note;
import org.gdg.ioext12.data.NoteDB;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public final class NewNoteServlet extends HttpServlet {

	private static final long serialVersionUID = 5573022822791703442L;
	private static final Logger logger = Logger.getLogger(NewNoteServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("doPost");
		final String content = req.getParameter("note");
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		if (user == null) {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		} else {
			if (content != null) {
				final NoteDB dao = new NoteDB();
				final Note note = new Note();
				note.setContent(content);
				note.setUser(user.getEmail());
				dao.persist(note);
				dao.close();
			}
		}
		//resp.sendRedirect("/notes.html");
	}
}