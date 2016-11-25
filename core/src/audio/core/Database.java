package audio.core;

import com.badlogic.gdx.files.FileHandle;
import com.musicg.wave.Wave;

import audio.database.BTree;

/**
 * 
 * @author TylerC
 *
 */
public class Database {

	private final String AUDIO_FOLDER = "D:/Audio Recoginition/Gradle/android/assets/files";
	private FileHandle fileFolder;

	private BTree<Integer, String> tree;
	
	public Database() {
		fileFolder = new FileHandle(AUDIO_FOLDER);
		tree = new BTree<Integer, String>();
	}

	/**
	 * Rebuilds the database of audio fingerprint's and frame data
	 * 
	 * @return
	 */
	public boolean rebuildDatabase() {
		System.out.println("Rebuilding database.... this may take a while");
		FileHandle[] i = fileFolder.list();
		for (FileHandle e : i) {
			System.out.println("Found file: " + e.name());
			Wave w = new Wave(e.path());
			long s = Analyzer.computeHash(Analyzer.getKeyPoints(w));
			System.out.println("Hash: " + s);
		}
		System.out.println("Database rebuilt");
		return true;
	}
}
