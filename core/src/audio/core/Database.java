package audio.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.musicg.wave.Wave;

public class Database {

	private final String AUDIO_FOLDER = Settings.AUDIO_FILES_LOC;
	private final String SONG_DATA = Settings.DATABASE_LOC + "song_data.txt";
	private final String SONG_NAMES = Settings.DATABASE_LOC + "song_names.txt";
	
	private FileHandle fileFolder;
	private Map<Integer, String> songName;
	private HashMap<Integer, List<SongPoint>> songData;
	private Map<Integer, Map<Integer, Integer>> matchMap;
	private static int nSongs = 0;

	public Database() {
		fileFolder = new FileHandle(AUDIO_FOLDER);
		songData = new HashMap<Integer, List<SongPoint>>();
		songName = new HashMap<Integer, String>();
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
			e.printStackTrace();
		}
	}

	/**
	 * loads already processed audio data
	 */
	public void load() {
		// Load song id's and song names
		System.out.println("Loading database.... ");
		try {
			File f = new File(SONG_NAMES);
			Scanner s = new Scanner(f);
			s.useDelimiter("\\d+");
			int i = 0;	
			while (s.hasNext()) {
				String n = null;
				n = s.next();
				System.out.print("ID : " + i + " Title: " + n);
				songName.put(i, n);
				i++;
			}
			nSongs = songName.size();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Loading all relevant data...");
		// load song data
		try {
			File f = new File(SONG_DATA);
			Scanner s = new Scanner(f);
			List<SongPoint> list;
			while (s.hasNextLine()) {
				String line = s.nextLine();
				Scanner lineScanner = new Scanner(line);
				lineScanner.useDelimiter("([^0-9]+)");
				lineScanner.skip("\\d{0,10}");
				list = new ArrayList<SongPoint>();
				int curHash = 0;
				while(lineScanner.hasNextInt()) {	
					int id = lineScanner.nextInt();
					int t = lineScanner.nextInt();
					int h = lineScanner.nextInt();
					SongPoint p = new SongPoint(id, t, h);
					list.add(p);
					curHash = p.hash;
				}
				lineScanner.close();
				songData.put(curHash, list);
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished loading! (Hashmap size: " + songData.size() + ")");
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
		save();
		return true;
	}
	
	/** NOT IMPLEMENTED 
	 * 
	 * public void buildSQL() {
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
		} 
	} **/
	
	/**
	 * Search by a recorded Wav.file through our processed audio in the database
	 * @param w
	 */
	public void search(Wave w) {
		long startTime = System.currentTimeMillis();
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
		
		Map<Integer, String> songs = new HashMap<Integer, String>();
		int bestSong = -1;
		int bestCount = 0;
		int total = 0;
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
				total++;
			}
			//System.out.print(songName.get(x) + "Score: " +  bestCountForSong + "\n");
		}
		long endTime = System.currentTimeMillis();
		
		
		int c = bestCount; // our answer

	
		int conf = (c / total);
		
		System.out.println("\n\nBest guess" + songName.get(bestSong) + "(search took" + (endTime - startTime) + "ms)");
		System.out.println("Confidence: " + conf);
	} 
	
	
	public void activeSearch(Wave w) {
		Map<Integer, String> songs = new HashMap<Integer, String>();
		
		
		
		
		
		
	}
}

