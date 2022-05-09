package chessgame.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.useVsync(false);
        cfg.setTitle("Corrupt Chess");
        cfg.setWindowedMode(1980, 1000);
        cfg.setForegroundFPS(350);
        cfg.setIdleFPS(30);
        //cfg.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        ChessGame game = new ChessGame();
        new Lwjgl3Application(game, cfg);
    }
}