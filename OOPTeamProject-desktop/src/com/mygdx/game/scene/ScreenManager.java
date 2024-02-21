package scene;
import com.mygdx.game.GameMaster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class ScreenManager {
    private static ScreenManager instance;
    GameMaster game;

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(GameMaster game) {
        this.game = game;
    }

    public void showMainScreen() {
        game.setScreen(new MainMenuScreen(game));
    }

    public void showPlayScreen() {
        game.setScreen(new PlayScreen(new SpriteBatch()));
    }

    public void showEndScreen() {
        game.setScreen(new EndScreen());
    }
}
