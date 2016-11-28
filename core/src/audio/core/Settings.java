package audio.core;

public class Settings {
	
	public static String DATABASE_LOC = "";
	
	public static String AUDIO_FILES_LOC = "";
	
	public static int RECORDING_TIME = 12000;
	
	// Increase to get more matches (better to increase slightly with bad quality microphones)
	public static final int ERROR_FACTOR = 1;
	
	// Change which frequency units are filtered (each microphone has strengths in certain frequencies)
	public static final int[] FILTER_BANK = new int[] {70, 120, 180, 220, 256};
	
	
}
