package legacy;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Song implements Json.Serializable {

	private String title;
	private short hashValue;

	public Song(String title, short hashValue) {
		this.title = title;
		this.hashValue = hashValue;
	}

	@Override
	public void write(Json json) {
		json.writeField(this.title, "Title");
		json.writeField(this.hashValue, "Hash");
	}	

	@Override
	public void read(Json json, JsonValue jsonData) {
		

	}

}
