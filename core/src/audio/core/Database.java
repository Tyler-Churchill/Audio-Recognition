package audio.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.musicg.wave.Wave;

public class Database {

	private final String AUDIO_FOLDER = "D:/Audio Recoginition/Gradle/android/assets/files";
	private FileHandle fileFolder;

	private Map<Integer, String> songName;
	private HashMap<Integer, List<SongPoint>> songData;
	private Map<Integer, Map<Integer, Integer>> matchMap;

	private static int nSongs = 0;
	
	private SQLDatabase sql;
	
	private final String SONG_DATA = "D:/Audio Recoginition/Gradle/android/assets/song_data.txt";
	private final String SONG_NAMES = "D:/Audio Recoginition/Gradle/android/assets/song_names.txt";
	
	public Database() {
		fileFolder = new FileHandle(AUDIO_FOLDER);
		songData = new HashMap<Integer, List<SongPoint>>();
		songName = new HashMap<Integer, String>();
		sql = new SQLDatabase();
	}

	/**
	 * Save all song information we need to match to files
	 */
	public void save() {
		// save our song titles with the key of id
		File fh = new File(SONG_NAMES);
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(fh));
			for(Map.Entry<Integer, String> e : songName.entrySet()) {
				b.write(e.getKey() + " " + e.getValue());
				b.newLine();
			}
			b.flush();
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// save our corresponding song data
		File f = new File(SONG_DATA);
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(f));
			for(Map.Entry<Integer, List<SongPoint>> e : songData.entrySet()) {
				b.write(e.getKey() + ":");
				List<SongPoint> p = e.getValue();
				for(SongPoint n : p) {
					b.write(n.toString() + ", ");
				}
				b.newLine();
			}
			b.flush();
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load() {
		System.out.println("Loading database.... ");
		try {
			File f = new File(SONG_NAMES);
			Scanner s = new Scanner(f);
			s.useDelimiter("\\s+");
			while(s.hasNext()) {
				int i = 0;
				String n = null;
				if(s.hasNextInt()) {
					i = s.nextInt();
				} else {
					n = s.next();
				}
				songName.put(i, n);
			}
			s.close();
			System.out.println("Loaded song id's and song names...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			File f = new File(SONG_DATA);
			Scanner s = new Scanner(f);
			s.useDelimiter("\\s+");
			while(s.hasNext()) {
			
			}
			s.close();
			System.out.println("Loaded song data...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished loading! (Hashmap size: " + songData.size()+ ")");
	}

	/**
	 * Rebuilds the database of audio fingerprint's and frame data
	 * 
	 * @return
	 */
	public boolean rebuildInternal() {
		System.out.println("Rebuilding database.... this may take a while");
		FileHandle[] i = fileFolder.list();
		List<SongPoint> points = null;
		// Go through every file in our directory
		for (FileHandle e : i) {
			System.out.print("\nFound file: " + e.name() + " Song ID: " + nSongs + " .....");
			songName.put(nSongs, e.name());
			Wave w = new Wave(e.path());
			List<SongPoint> p = Analyzer.getKeyPoints(nSongs, w);
			nSongs++;
			for (SongPoint n : p) {
				if ((points = songData.get(n.hash)) == null) {
					//no existing hash, create new bucket of song points
					points = new ArrayList<SongPoint>();
					points.add(n);
					songData.put(n.hash, points);
				} else {
					//found existing hash, add data to bucket
					points.add(n);
				}
			}
			System.out.print("finished processing");
		}
		System.out.println("\n\nDatabase rebuilt! (Hashmap size: " + songData.size()+ ")");
		return true;
	}
	
	
	
	public void buildSQL() {
		//sql.deleteAllSongList();
		//sql.deleteAllSongPoint();
		//sql.checkTables();
		System.out.println("Rebuilding SQL database.... this may take a while");
		FileHandle[] i = fileFolder.list();
		for (FileHandle e : i) {
			System.out.print("\nFound file: " + e.name() + " Song ID: " +nSongs + " .....");
			Wave w = new Wave(e.path());
			List<SongPoint> p = Analyzer.getKeyPoints(nSongs, w);
			for(SongPoint n : p)
				songData.put(n.hash, p);
			nSongs++;

			for (Entry<Integer, List<SongPoint>> n : songData.entrySet()) {
				sql.insertSongData(n.getValue());
			}
			
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
					if ((temp = this.matchMap.get(tp.songID)) == null) {
						temp = new HashMap<Integer, Integer>();
						temp.put(off, 1);
						//System.out.println("ADDED TO MATCHMAP: " + tp.songID + " TEMP" + temp);
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
		// now find best matches from our match map which has our recorded audio data
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
			}
			if (bestCountForSong > bestCount) {
				bestCount = bestCountForSong;
				bestSong = x;
			}
			System.out.println("SongID = " + x + " Score: " +  bestCountForSong);
		}
		System.out.println("\n\nBest guess song: " + bestSong);
	} 
}

