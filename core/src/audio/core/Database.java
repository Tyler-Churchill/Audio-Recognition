package audio.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.musicg.wave.Wave;

/**
 * 
 * @author TylerC
 *
 */
public class Database {

	private final String AUDIO_FOLDER = "D:/Audio Recoginition/Gradle/android/assets/files";
	private FileHandle fileFolder;

	// song id's and song names
	
	private HashMap<Long, List<SongPoint>> songData;
	private Map<Integer, Map<Integer, Integer>> matchMap;

	private static int nSongs = 0;
	
	
	public Database() {
		fileFolder = new FileHandle(AUDIO_FOLDER);
		songData = new HashMap<Long, List<SongPoint>>();
		matchMap = new HashMap<Integer, Map<Integer, Integer>>();
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
			
			List<SongPoint> p = Analyzer.getKeyPoints(nSongs, w);
			nSongs++;			
			for(SongPoint n : p)
				songData.put(n.hash, p);
			
		}
		System.out.println("Database rebuilt, Hashmap size: " + songData.size());
		return true;
	}
	
	public void search(Wave w) {
		
		List<SongPoint> p = Analyzer.getKeyPoints(nSongs, w);
		List<SongPoint> listPoints;
		
		for (SongPoint sp : p) {

			System.out.println("Recorded Hash: " + sp.hash);
			
			if ((listPoints = songData.get(sp.hash)) != null) {
				System.out.println("HIT: " + sp.hash);
				
				for (SongPoint tp : listPoints) {
					int off = tp.time;
		
					Map<Integer, Integer> temp = null;
					if ((temp = this.matchMap.get(tp.songID)) == null) {
						temp = new HashMap<Integer, Integer>();
						temp.put(off, 1);
						matchMap.put(tp.songID, temp);
					} else {
						Integer count = temp.get(off);
						if (count == null) {
							temp.put(off, new Integer(1));
						} else {
							temp.put(off, new Integer(count + 1));
						}
					}
				}
			}
		}
		
		int bestSong = -1;
		int bestCount = 0;
		
		for (int x = 0; x < nSongs; x++) {
			System.out.println("For song id: " + x);
			int bestCountForSong = 0;
			Map<Integer, Integer> tmpMap = matchMap.get(x);
			
			if(tmpMap != null)
			for (Map.Entry<Integer, Integer> e : tmpMap.entrySet()) {
				if (e.getValue() > bestCountForSong) {
					bestCountForSong = e.getValue();
				}
				//System.out.println("Time offset = " + e.getKey()
				//+ ", Count = " + e.getValue());
			}
			if (bestCountForSong > bestCount) {
				bestCount = bestCountForSong;
				bestSong = x;
			}
			
			System.out.print("bestcountforsong: " + bestCountForSong);
		}
	
		System.out.println("Best id: " + bestSong);
		
	}
	
	public void insert() {
		
	}
	
}

