package chessgame.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import chessgame.menues.MenuScreen;
import chessgame.utils.Constants;
import chessgame.utils.SaveFile;


public class ChessGame extends Game {

	public SpriteBatch batch;
	public BitmapFont font;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font
        Vector2 resolution = Constants.resolutions[SaveFile.readSettings()[5]];
        Gdx.graphics.setWindowedMode((int)resolution.x, (int)resolution.y);
        if (SaveFile.readSettings()[6] == 1) {
        	Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
		this.setScreen(new MenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

}