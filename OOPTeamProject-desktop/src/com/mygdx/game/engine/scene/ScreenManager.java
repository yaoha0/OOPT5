package engine.scene;
import com.mygdx.game.GameMaster;

import game.screens.EndScreen;
import game.screens.MainMenuScreen;
import game.screens.PlayScreen;
import engine.simulationLC.SimulationLifeCycle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class ScreenManager {
    private static ScreenManager instance;
    private GameMaster game;
    private SpriteBatch batch;
    private SimulationLifeCycle simulationLifeCycle;
    private MainMenuScreen mainMenuScreen; // Add this line

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(GameMaster game, SpriteBatch batch, SimulationLifeCycle simulationLifeCycle) {
        this.game = game;
        this.batch = batch;
        this.simulationLifeCycle = simulationLifeCycle;
        this.mainMenuScreen = new MainMenuScreen(game); // Initialize the MainMenuScreen here
    }

    public MainMenuScreen getMainMenuScreen() { // Add this method to get the MainMenuScreen
        return mainMenuScreen;
    }

    public void showMainScreen() {
        game.setScreen(mainMenuScreen);
    }

    public void showPlayScreen() {
        game.setScreen(new PlayScreen(new SpriteBatch()));
    }

    public void showEndScreen() {
        game.setScreen(new EndScreen(batch, simulationLifeCycle));
    }
}
