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
			Wave w = new Wave("D:/Audio Recoginition/Gradle/android/assets/files/PolarBear-Puscifer.wav");
			database.search(w);
		}

		if (Gdx.input.isKeyJustPressed(Keys.L)) {
			database.load();
		}

		if (Gdx.input.isKeyJustPressed(Keys.B)) {
			database.rebuildInternal();
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
