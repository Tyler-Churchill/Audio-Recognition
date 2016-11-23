package audio.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * @author TylerC
 *
 */
public class Database {

	private final String AUDIO_FOLDER = Gdx.files.internal("files/").path();

	private FileHandle fileFolder;

	public Database() {
		fileFolder = new FileHandle(AUDIO_FOLDER);
	}

	/**
	 * Rebuilds the database of audio fingerprint's and frame data
	 * 
	 * @return
	 */
	public boolean rebuildDatabase() {	
		System.out.println("Rebuilding database.... this may take a while");
		FileHandle[] i = fileFolder.list();
		for(FileHandle e : i) {
			System.out.println("Found file: " + e.name());
	
		}
		System.out.println("Database rebuilt");
		return true;
	}
	

	
	
}
