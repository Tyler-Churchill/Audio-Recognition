package audio.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Core extends ApplicationAdapter {

	private AudioRecorder recorder;
	private short[] pcm = new short[1024];
	private RecordInputProcessor input;
	private ShapeRenderer renderer;
	private OrthographicCamera camera;
	private FFTAnalyze analyzer;

	@Override
	public void create() {
		recorder = Gdx.audio.newAudioRecorder(44100, true);
		input = new RecordInputProcessor();
		Gdx.input.setInputProcessor(input);
		renderer = new ShapeRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		analyzer = new FFTAnalyze(pcm);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (input.keyPressed) {
			recorder.read(pcm, 0, pcm.length);
			renderer.begin(ShapeType.Line);
			renderer.setColor(1,1,0,1);
			int offset = 10;
			int prevX = 0;
			int prevY = 0;
			for (short e : pcm) {
				int d = e % 50;
				renderer.line(prevX, prevY, prevX+offset, d);
				prevX = prevX + offset;
				prevY = d;
			}
			 renderer.end();
			 
	
			 // spawn fft analyze thread and analyze incoming data and get results from the result pool
			 
		} else if(!input.keyPressed) {
		
		} else {
			
		}
	}
	
	   @Override
	   public void resize(int width, int height){
	      camera.viewportWidth = width;
	      camera.viewportHeight = height;
	      camera.update();
	   }

	@Override
	public void dispose() {
		recorder.dispose();
		renderer.dispose();
	}
}
