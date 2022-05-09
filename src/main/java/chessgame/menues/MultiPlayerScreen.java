package chessgame.menues;

import chessgame.app.ChessGame;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MultiPlayerScreen implements Screen{

    OrthographicCamera cam;
    final ChessGame game;
    private Stage stage;

    public MultiPlayerScreen(ChessGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Background image
        Table backgroundTable = new Table();
        backgroundTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("assets/background.png"))));
        backgroundTable.setFillParent(true);
        stage.addActor(backgroundTable);

        Label title = UI.label(new Vector2(24,2), new Vector2(0,14), "Client OR Host", "title-light");
        stage.addActor(title);

        //Button back to Title screen
        Button backButton = UI.newScreenButton(new Vector2(3,1.5f), new Vector2(10.5f,1), "Back", ScreenType.MenuScreen, game, 0);
        stage.addActor(backButton);

        Button hostButton = UI.newScreenButton(new Vector2(4, 1.5f), new Vector2(6.2f, 7), "Host", ScreenType.HostScreen, game, 0);
        stage.addActor(hostButton);

        Button clientButton = UI.newScreenButton(new Vector2(4, 1.5f), new Vector2(14.2f, 7), "Join", ScreenType.ClientScreen, game, 0);
        stage.addActor(clientButton);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

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
    }
}