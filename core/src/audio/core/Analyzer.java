package audio.core;

import java.util.Arrays;

import com.musicg.fingerprint.FingerprintManager;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

public class Analyzer {

	private static FingerprintManager manager;

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
	
	public static int getHash(Object[] o) {
		return Arrays.deepHashCode(o);
	}
	
	public static FingerprintSimilarity getSimilarity(Wave x, Wave n) {
		return x.getFingerprintSimilarity(n);
	}
	
	public short[] getRecData(short[] pcm) {
		return pcm;
	}
}
