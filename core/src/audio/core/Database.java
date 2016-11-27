package audio.core;

import java.util.ArrayList;
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
	
	private HashMap<Integer, List<SongPoint>> songData;
	private Map<Integer, Map<Integer, Integer>> matchMap;

	private static int nSongs = 0;
	
	private SQLDatabase sql;
	
	
	
	public Database() {
		fileFolder = new FileHandle(AUDIO_FOLDER);
		songData = new HashMap<Integer, List<SongPoint>>();
		sql = new SQLDatabase();
	}

	/**
	 * Rebuilds the database of audio fingerprint's and frame data
	 * 
	 * @return
	 */
	public boolean rebuildDatabase() {
		System.out.println("Rebuilding database.... this may take a while");
		FileHandle[] i = fileFolder.list();
		List<SongPoint> points = null;
		for (FileHandle e : i) {
			System.out.print("\nFound file: " + e.name() + " Song ID: " + nSongs + " .....");
			Wave w = new Wave(e.path());
			List<SongPoint> p = Analyzer.getKeyPoints(nSongs, w);
			nSongs++;

		
			for (SongPoint n : p) {
			
				if ((points = songData.get(n.hash)) == null) {
					System.out.println("New hash found: " + n.hash);
					points = new ArrayList<SongPoint>();
					points.add(n);
					songData.put(n.hash, points);
				} else {
					System.out.println("Existing hash found: " + n.hash);
					points.add(n);
				}
				System.out.println("Points: " + points.size());
			}

			System.out.print("finished processing");
		}
		
		System.out.println("\n\nDatabase rebuilt! (Hashmap size: " + songData.size() + ")");
		
		return true;
	}
	
	
	
	
	
	
	
	
	
	public void buildSQL() {
		sql.deleteAllSongList();
		sql.deleteAllSongPoint();
		System.out.println("Rebuilding SQL database.... this may take a while");
		FileHandle[] i = fileFolder.list();
		for (FileHandle e : i) {
			System.out.print("\nFound file: " + e.name() + " Song ID: " +nSongs + " .....");
			Wave w = new Wave(e.path());
			List<SongPoint> p = Analyzer.getKeyPoints(nSongs, w);
			for(SongPoint n : p)
				songData.put(n.hash, p);
		
			nSongs++;
		}
	}
	
	

	
	public void search(Wave w) {
		
		List<SongPoint> p = Analyzer.getKeyPoints(nSongs, w);
		matchMap = new HashMap<Integer, Map<Integer, Integer>>();
		
		for (SongPoint sp : p) {
			List<SongPoint> list;
			if ((list = songData.get(sp.hash)) != null) {
		
				
				for (SongPoint tp : list) {
					
					Map<Integer, Integer> temp = null;
					int off = Math.abs(tp.time - sp.time);
					
				 
					System.out.println("SID " + tp.songID);
					if ((temp = this.matchMap.get(tp.songID)) == null) {
						temp = new HashMap<Integer, Integer>();
						temp.put(off, 1);
						System.out.println("ADDED TO MATCHMAP: " + tp.songID + " TEMP" + temp);
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
			
			System.out.println("SongID = " + x + " Score: " +  bestCountForSong);
		}
	
		System.out.println("\n\nBest guess song: " + bestSong);
		
	} 
	
	public void insert() {
		
	}
	
}

