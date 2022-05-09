package chessgame.world;

import chessgame.utils.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class TiledGameMap extends GameMap{
    static int PPM = Constants.PixelPerMeter;
    
	public TiledMap tiledMap;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	MapProperties properties;
	
	public TiledGameMap(String map) {
		
		tiledMap = new TmxMapLoader().load(Gdx.files.internal("assets/levels/"+map+".tmx").file().getAbsolutePath());
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f/PPM);
		properties = tiledMap.getProperties();
	}
	
	@Override
	public void render(OrthographicCamera camera) {
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		tiledMap.dispose();
		
	}
	@Override
	/**
	 * Returns the width of the map in Tiles.
	 */
	public int getWidth() {
		// TODO Auto-generated method stub
		return properties.get("width", Integer.class);
	}
	
	/**
	 * Returns the width of the map in Pixels.
	 */
	public int getWidthPixels() {
		return getWidth()*32;
	}
	
	/**
	 * Returns the height of the map in Tiles.
	 */
	@Override
	public int getHeight() {
		return properties.get("height", Integer.class);
	}
	
	/**
	 * Returns the height of the map in Pixels.
	 */
	public int getHeightPixels() {
		return getWidth()*32;
	}

	@Override
	public MapLayers getLayers() {
		return tiledMap.getLayers();
	}

	@Override
	public void changeMap(String name) {
		tiledMap = new TmxMapLoader().load(Gdx.files.internal("assets/"+name+".tmx").file().getAbsolutePath());
		tiledMapRenderer.setMap(tiledMap);
	}

	@Override
	public Vector2 getStartPoint() {
		return new Vector2(200,200);
	}

}
