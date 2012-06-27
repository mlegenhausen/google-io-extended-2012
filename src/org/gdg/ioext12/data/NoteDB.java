package org.gdg.ioext12.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.gdg.ioext12.util.PMF;

public final class NoteDB implements NoteDAO{

	/**
	 * used to call find functions.
	 */
	private PersistenceManager pManager = PMF.get().getPersistenceManager();
	
	@Override
	public Note persist(final Note note) {
		final PersistenceManager pManager = PMF.get().getPersistenceManager();
		Note result = null;
		try{
			if(note.getCreatedAt() != null)
				note.setCreatedAt(new Date());
			note.setUpdatedAt(new Date());
		    result = pManager.makePersistent(note);
		    pManager.flush();
		}finally{
			pManager.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Note> findByUser(final String user) {
		final Query query = pManager.newQuery(Note.class, "this.user == user");
		query.declareParameters("String user");
		List<Note> noteList = (List<Note>) query.execute(user);
 		if(noteList == null){
 			noteList = new ArrayList<Note>();
 		}
		return noteList;
	}

	@Override
	public void close() {
		this.pManager.close();
	}
}
