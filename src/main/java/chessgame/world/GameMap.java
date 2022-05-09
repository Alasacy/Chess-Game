package chessgame.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;

public abstract class GameMap {
	
	public abstract void render (OrthographicCamera camera);
	public abstract void update (float delta);
	public abstract void dispose ();
	/**
	 * Changes the current map to another map
	 * @param name - the name of the map to change to.
	 */
	public abstract void changeMap(String name);
	/**
	 * Gets the start position of Player
	 * @return the start position
	 */
	public abstract Vector2 getStartPoint();
	
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract MapLayers getLayers();
	public abstract int getWidthPixels();
	public abstract int getHeightPixels();
	
}
