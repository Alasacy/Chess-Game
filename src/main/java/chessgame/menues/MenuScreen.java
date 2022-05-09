package chessgame.menues;

import chessgame.app.ChessGame;
import chessgame.utils.GameSound;
import chessgame.utils.ScreenType;
import chessgame.utils.UI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class MenuScreen implements Screen {

	OrthographicCamera cam;
    final ChessGame game;
    private Stage stage;
//    public static boolean killServer = false;
		
	public MenuScreen(ChessGame game) {
		
		this.game = game;
		stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
       
		GameSound.stopMusic();
        
        //Background image
        Table backgroundTable = new Table();
        backgroundTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("assets/background.png"))));
        backgroundTable.setFillParent(true);
        stage.addActor(backgroundTable);
        
        //Logo / title
        Image logo = UI.image(new Vector2(5,4), new Vector2(3,12), "assets/corruptChess.png");
        stage.addActor(logo);      
        
        //Play button for starting the game.
        Button playButton = UI.playButton(new Vector2(4, 1.5f), new Vector2(4, 10), game);
        stage.addActor(playButton);

		//multiplayer button for starting the game.
		Button multiPlayerButton = UI.newScreenButton(new Vector2(4, 1.5f), new Vector2(4, 4), "Multiplayer", ScreenType.MultiPlayerScreen, game, 0);
		stage.addActor(multiPlayerButton);
        
        //Button for going to the option screen.
        Button optionButton = UI.newScreenButton(new Vector2(4, 1.5f), new Vector2(4, 8.5f), "Options", ScreenType.OptionScreen, game, 0);
        stage.addActor(optionButton);
        
        //Currently unused button
        Button howToPlayButton = UI.newScreenButton(new Vector2(4, 1.5f), new Vector2(4, 7), "How to play", ScreenType.HowToPlay, game, 0);
        stage.addActor(howToPlayButton);
        
        //Button for exiting the game
        Button quitButton = UI.exitButton(new Vector2(4, 1.5f), new Vector2(4, 5.5f));
        stage.addActor(quitButton);
        
        
        //Button for resetting the game
        Button resetButton = UI.resetButton(new Vector2(4,2), new Vector2(20,0));
        stage.addActor(resetButton);
        
	}
	
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

//        if (killServer){
//
//		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
