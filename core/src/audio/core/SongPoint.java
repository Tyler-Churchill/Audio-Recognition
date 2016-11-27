package audio.core;

public class SongPoint {
	int time; //
	int songID;
	Integer hash;

	public SongPoint(int songID, int time) {
		this.time = time;
		this.songID = songID;
		this.hash = hash;
	}
	
	@Override
	public String toString() {
		return "i" + songID + "t" + time + "h" + hash;
	}
	
}