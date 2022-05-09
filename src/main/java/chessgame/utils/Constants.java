package chessgame.utils;

import com.badlogic.gdx.math.Vector2;

public class Constants {
	//TileInfo
	public static final int PixelPerMeter = 32;	
	public static final float Gravity = 50f;
	public static final int worldWidth = 100;
	public static final int worldHeight = 100;
	//ScreenSizes
	public static final String[] resolutionsString = {" 1366x768"," 1600x900"," 1920x1080"};
	public static final Vector2[] resolutions = {new Vector2(1366,768),new Vector2(1600,900),new Vector2(1920,1080)};
}
