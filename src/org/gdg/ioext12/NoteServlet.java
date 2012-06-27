package org.gdg.ioext12;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
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

public class NoteServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7845565559852964098L;
	private static final Logger logger = Logger.getLogger(NoteServlet.class.getName());
	
	/*
	 * Delete note
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doDelete(req, resp);
	}
	
	/*
	 * Return note
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		
		if(user== null){
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.getWriter().append(("{url:"+req.getRequestURI()+"}"));
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

	/* Update note
	 * 
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String content = req.getParameter("content");
		String id = req.getRequestURI();
		logger.info("doPut");
		logger.info("RequestURI:"+req.getRequestURI());
		final String key = id.substring(id.lastIndexOf("/"));
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		if(user== null){
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.getWriter().append(("{url:"+req.getRequestURI()+"}"));
		}else{
			if (content != null){
				final NoteDAO dao = new NoteDB();
				final List<Note> notes = dao.findByUser(user.getEmail());
				for (Note note: notes){
					if (note.getKey().toString() == key){
						note.setContent(content);
						dao.persist(note);
						dao.close();
					}
				}
			}
		}
		
	}
	
	
	/*
	 * creating new note
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String content = req.getParameter("content");
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		if (user == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.getWriter().append(("{url:"+req.getRequestURI()+"}"));
		} else {
			if (content != null) {
				final NoteDAO dao = new NoteDB();
				final Note note = new Note();
				note.setContent(content);
				note.setUser(user.getEmail());
				dao.persist(note);
				dao.close();
			}
		}
	}

}
