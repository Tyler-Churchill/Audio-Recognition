package audio.core;

import com.badlogic.gdx.Gdx;

import audio.core.math.Complex;

public class FFTAnalyze implements Runnable {

	private int CHUNK_SIZE = 32;
	private short[] pcm;
	final int possible;
	Complex[][] results;

	public FFTAnalyze(short[] pcm) {
		this.pcm = pcm;
		possible = pcm.length / CHUNK_SIZE;
		results = new Complex[possible][];
	}

	@Override
	public void run() {

		System.out.println("Analyzing with FFT....");

		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				for (int x = 0; x < possible; x++) {
					Complex[] complex = new Complex[CHUNK_SIZE];
					for (int i = 0; i < CHUNK_SIZE; i++) {
						complex[i] = new Complex(pcm[(x * CHUNK_SIZE) + i], 0);
					}
					// results[x] = FFT.fft(complex);
				}

			}
		});
	}
}
