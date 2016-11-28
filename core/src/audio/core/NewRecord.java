package audio.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.badlogic.gdx.files.FileHandle;
import com.musicg.wave.Wave;

public class NewRecord {

	private static final int BUFFER_SIZE = 4096;
	private ByteArrayOutputStream recordBytes;
	private TargetDataLine audioLine;
	private AudioFormat format;
	private final String temp = "D:/Audio Recoginition/Gradle/android/assets/tmp/newrecord.wav";
	private int RECORD_TIME = Settings.RECORDING_TIME;

	private boolean isRunning;
	


	/**
	 * Defines a default audio format used to record
	 */
	AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	
	
	
	public Wave record() {
	
		audioLine = null;
		format = null;
		
		Thread stopper = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(RECORD_TIME);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				try {
					stop();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	
		stopper.start();
		System.out.println("Started recording...");
		try {
			start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Stopped recording...");
			return save();
	
	}
	
	

	/**
	 * Start recording sound.
	 * 
	 * @throws LineUnavailableException
	 *             if the system does not support the specified audio format nor
	 *             open the audio data line.
	 */
	public void start() throws LineUnavailableException {
		format = getAudioFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		// checks if system supports the data line
		if (!AudioSystem.isLineSupported(info)) {
			throw new LineUnavailableException("The system does not support the specified format.");
		}

		audioLine = AudioSystem.getTargetDataLine(format);

		audioLine.open(format);
		audioLine.start();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = 0;

		recordBytes = new ByteArrayOutputStream();
		isRunning = true;

		while (isRunning) {
			bytesRead = audioLine.read(buffer, 0, buffer.length);
			recordBytes.write(buffer, 0, bytesRead);
		}
	}

	/**
	 * Stop recording sound.
	 * 
	 * @throws IOException
	 *             if any I/O error occurs.
	 */
	public void stop() throws IOException {
		isRunning = false;
		
		if (audioLine != null) {
			audioLine.drain();
			audioLine.close();
		}
	}
	
	
	public void cleanUpTemp() {
		FileHandle h = new FileHandle(temp);
		FileHandle all[] = h.list();
		for(FileHandle x : all)
			x.delete();
	}
	

	/**
	 * Save recorded sound data into a .wav file format.
	 * 
	 * @param wavFile
	 *            The file to be saved.
	 * @throws IOException
	 *             if any I/O error occurs.
	 */
	public Wave save() {
		File wavFile = new File(temp);
		byte[] audioData = recordBytes.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
		AudioInputStream audioInputStream = new AudioInputStream(bais, format,
				audioData.length / format.getFrameSize());

		try {
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);
			audioInputStream.close();
			recordBytes.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Wave w = new Wave(temp);
		
		cleanUpTemp();
		return w;
	}
}