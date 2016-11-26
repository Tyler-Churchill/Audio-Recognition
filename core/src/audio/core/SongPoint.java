package audio.core;

public class SongPoint {
	int time;
	int songID;
	long hash;

	public SongPoint(int songID, int time, long hash) {
		this.time = time;
		this.songID = songID;
		this.hash = hash;
	}
	
}