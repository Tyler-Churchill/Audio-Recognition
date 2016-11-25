package audio.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.musicg.wave.Wave;

public class Core extends ApplicationAdapter {

	private OrthographicCamera camera;
	private Database database;
	private RecordAudio rec;
	private Analyzer analyzer;
	
	@Override
	public void create() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		database = new Database();
		rec = new RecordAudio();
		analyzer = new Analyzer();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			Wave w = rec.record(analyzer);
			System.out.println("Recorded hash: " + Analyzer.computeHash(Analyzer.getKeyPoints(w)));
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.B)) {
			database.rebuildDatabase();
		}

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void dispose() {
		
	}
}


/**
 * 
 * 
 * 	
		 * recorder.read(pcm, 0, pcm.length); byte[] b = new byte[pcm.length];
		 * for(int x = 0; x < pcm.length; x++) b[x] = (byte) pcm[x]; InputStream
		 * in = new ByteArrayInputStream(b); Wave w = new Wave(in);
		 * analyzer.getFingerprint(w); return analyzer.getFingerprint(w);
		 *
 * 
 * 
 * 
 * recorder.read(pcm, 0, pcm.length);
 * 
 * renderer.begin(ShapeType.Line); renderer.setColor(1,1,0,1); int
 * offset = 5; int prevX = 0; int prevY = 0;
 * 
 * 
 * for (short e : pcm) { int d = e % 50; renderer.line(prevX, prevY,
 * prevX+offset, d); prevX = prevX + offset; prevY = d; }
 * 
 * 
 * 
 * renderer.end();
 * 
 * 
 * // spawn fft analyze thread and analyze incoming data and get results
 * from the result pool
 **/
