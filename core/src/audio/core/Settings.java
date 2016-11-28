package audio.core;

public class Settings {
	
	public static String DATABASE_LOC = "D:/Audio Recoginition/Gradle/android/assets/";
	
	public static String AUDIO_FILES_LOC = "D:/Audio Recoginition/Gradle/android/assets/files/";
	
	public static int RECORDING_TIME = 12000;
	
	public final static int LOWER_LIMIT = 30;
	
	public final static int UPPER_LIMIT = 255;
	
	// Increase to get more matches (better to increase slightly with bad quality microphones)
	public static final int ERROR_FACTOR = 1;
	
	// Change which frequency units are filtered (each microphone has strengths in certain frequencies)
	public static final int[] FILTER_BANK = new int[] {70, 120, 180, 220, 256};
	//RANGE = new int[] {40, 80, 120, 160, UPPER_LIMIT + 1};
	
	// skip first 30 frames - weird header data
	public static int SONG_START = 30;
	
	
}	
