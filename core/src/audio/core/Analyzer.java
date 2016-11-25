package audio.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.musicg.fingerprint.FingerprintManager;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;
import com.musicg.wave.extension.Spectrogram;

public class Analyzer {

	private static FingerprintManager manager;
	private static Spectrogram spec;

	public static final int ERROR_FACTOR = 2;
	public final static int LOWER_LIMIT = 10;
	public final static int UPPER_LIMIT = 256;

	public static final int[] RANGE = new int[] {40, 80, 120, 180, UPPER_LIMIT};

	public Analyzer() {
		manager = new FingerprintManager();
	}

	public static byte[] getFingerprint(Wave wave) {
		return manager.extractFingerprint(wave);
	}

	public static Object[] getAudioFramesData(String file) {
		Wave w = new Wave(file);
		byte[] b = getFingerprint(w);
		Object[] a = new Object[b.length];
		int n = -1;
		for(byte x : b) {
			n +=1;
			a[n] = x & 0xFF;
		}
		return a;
	}	

	/**
	 * Gets the keypoints/features of a song and returns a hash map
	 * HashMap<time><magnitude>
	 * 
	 * @param w
	 * @return
	 */
	public static long[] getKeyPoints(Wave w) {
		spec = new Spectrogram(w);
		// double[frame][freq]
		// double[size.numFrames][size.numFrequencyUnit]
		double data[][] = spec.getAbsoluteSpectrogramData();
		long keyPoints[] = new long[5];

		for (int x = 0; x < spec.getNumFrames(); x++) {
			// holds frequency
			double temp[] = new double[spec.getNumFrequencyUnit()];
			int highScore[] = new int[5];
			int points[] = new int[5];

			for (int y = LOWER_LIMIT; y < spec.getNumFrequencyUnit(); y++) {
				temp[y] = data[x][y];

				int mag = (log(temp[y], 2));
				int index = getIndex(y);

				if (mag > highScore[index]) {
					highScore[index] = mag;
					points[index] = y;
				}
			}
			for (int n = 0; n < points.length; n++)
			{
				keyPoints[n] = points[n];
			}
		}
		return keyPoints;
	}
	
	public static long computeHash(long[] keyPoints) {
			long p1 = keyPoints[0];
			long p2 = keyPoints[1];
			long p3 = keyPoints[2];
			long p4 = keyPoints[3];
		 return  (p4-(p4%ERROR_FACTOR)) * 100000000 + (p3-(p3%ERROR_FACTOR)) * 100000 + (p2-(p2%ERROR_FACTOR)) * 100 + (p1-(p1%ERROR_FACTOR));
	}
	
	
	
	static int log(double temp, int base)
	{
	    return (int) (Math.log(temp) / Math.log(base));
	}
	
	public static int getIndex(double data) {
		int i = 0;
		while (RANGE[i] < data)
			i++;
		return i;
	}

	
	public static int getHash(Object[] bs) {
		return Arrays.deepHashCode(bs);
	}
	

	
}
