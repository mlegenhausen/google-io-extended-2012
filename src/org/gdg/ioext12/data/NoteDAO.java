package org.gdg.ioext12.data;

import java.util.List;

public interface NoteDAO {

	Note persist(Note note);
	
	List<Note> findByUser(String user);
	
	void close();
	
}
