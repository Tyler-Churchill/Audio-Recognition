package workingfiles;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ContentHandler;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.xml.sax.helpers.DefaultHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class AudioLoader {
	
	
	private FileHandle loc;
	
	public AudioLoader(FileHandle locationOfAudio) {
		this.loc = locationOfAudio;
	}

	
	
	public boolean load(String fileLoc) {
		
		// File file = Gdx.files.local("../android/assets/" + fileLoc).file();
		File f = new File("D:/Audio Recoginition/Gradle/android/assets/" + fileLoc);

		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(f);

			AudioInputStream din = null;
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
					baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);

			OutputStream out = new ByteArrayOutputStream();
			boolean running = true;

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = din.read(buffer)) > -1) {
			    out.write(buffer, 0, bytesRead);
			}
			
			din.close();
			in.close();
			
			for(byte b : buffer)
				System.out.println("" + b);
			
			
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public String getAudioTitle(FileHandle handle) {
		return "";
	}
}
