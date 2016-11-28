package audio.core;

public class SongPoint {
	public int time; //
	public int songID;
	public Integer hash;

	public SongPoint(int songID, int time, int hash) {
		this.time = time;
		this.songID = songID;
		this.hash = hash;
	}
	
	@Override
	public String toString() {
		return "i" + songID + "t" + time + "h" + hash;
	}
	
}