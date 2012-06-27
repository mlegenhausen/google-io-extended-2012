package org.gdg.ioext12;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gdg.ioext12.data.Note;
import org.gdg.ioext12.data.NoteDAO;
import org.gdg.ioext12.data.NoteDB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public final class NotesServlet extends HttpServlet {
	
	private static final long serialVersionUID = -2024003164184708150L;

	/**
     * static attribute used for logging.
     */
    private static final Logger logger = Logger.getLogger(NotesServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		
		if(user== null){
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}else{
			final NoteDAO dao = new NoteDB();
			final List<Note> notes = dao.findByUser(user.getEmail()); 
			String result = null;
			try {
				result = this.createOutputArray(notes);
			} catch (JSONException e) {
				logger.warning("JSONException occured: " + e.getMessage());
			}
			dao.close();
			
			resp.setHeader("Content-Type", "text/javascript; charset=utf-8");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().append(result);
		}
	}
	
	
	private String createOutputArray(final List<Note> noteList) throws JSONException{
		final JSONArray array = new JSONArray();
		for(int i=0; i<noteList.size(); i++){
			final JSONObject object = new JSONObject();
			object.put("user", noteList.get(i).getUser());
			object.put("content", noteList.get(i).getContent());
			array.put(object);
		}
		return array.toString();
	}
}