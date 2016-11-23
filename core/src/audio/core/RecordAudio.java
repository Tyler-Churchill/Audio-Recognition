package audio.core;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

public class RecordAudio {

	private final int RECORD_TIME = 6000;

	private TargetDataLine line;
	private int fileN = 0;
	private final String temp = Gdx.files.internal("tmp/").path();
	
	public Wave record(Analyzer analyzer) {
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			Thread stopper = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(RECORD_TIME);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					finish();
				}
			});
			
			stopper.start(); // start the recording
			fileN += 1;
			String path = temp + "t" + fileN + ".wav";
			
			try {
				AudioFormat format = getFormat();
				DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
				
				File wavFile = new File(path);

				AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
				if (!AudioSystem.isLineSupported(info)) {
					System.out.println("Line not supported");
					System.exit(0);
				}
				line = (TargetDataLine) AudioSystem.getLine(info);
				line.open(format);
				line.start(); 
				System.out.println("Start capturing...");
				AudioInputStream ais = new AudioInputStream(line);
				System.out.println("Start recording...");
				AudioSystem.write(ais, fileType, wavFile);
			} catch (LineUnavailableException ex) {
				ex.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			/// TESTTTTTTTT
			Wave w = new Wave(path);
			Wave x = new Wave("D:/Audio Recoginition/Gradle/android/assets/files/Get Out.wav");
			FingerprintSimilarity s = Analyzer.getSimilarity(w, x);
			
			System.out.println("clip is found at "
					+ s.getsetMostSimilarTimePosition() + "s in "
					+ "get out with similarity " + s.getSimilarity());
			// END TEST
			
			
			return w;
		}
		
		return null;
		

		/**
		 * recorder.read(pcm, 0, pcm.length); byte[] b = new byte[pcm.length];
		 * for(int x = 0; x < pcm.length; x++) b[x] = (byte) pcm[x]; InputStream
		 * in = new ByteArrayInputStream(b); Wave w = new Wave(in);
		 * analyzer.getFingerprint(w); return analyzer.getFingerprint(w);
		 **/

	}

	public void finish() {
		line.stop();
        line.close();
        cleanUpTemp();
        System.out.println("Finished");
	}
	
	public void cleanUpTemp() {
		FileHandle h = new FileHandle(temp);
		FileHandle all[] = h.list();
		for(FileHandle x : all)
			x.delete();
	}
		
	private AudioFormat getFormat() {
		float sampleRate = 22050;
		int sampleSizeInBits = 8;
		int channels = 2; // mono
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	

}
