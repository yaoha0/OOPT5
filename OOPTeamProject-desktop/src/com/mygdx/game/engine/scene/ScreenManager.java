package engine.scene;
import com.mygdx.game.GameMaster;

import game.screens.EndScreen;
import game.screens.IntroScreen;
import game.screens.MainMenuScreen;
import game.screens.PlayScreen;
import engine.simulationLC.SimulationLifeCycle;
import engine.ioInput.InputOutputManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class ScreenManager {
    private static ScreenManager instance;
    private GameMaster game;
    private SpriteBatch batch;
    private SimulationLifeCycle simulationLifeCycle;
    private MainMenuScreen mainMenuScreen; 
    private IntroScreen introScreen;
    private InputOutputManager inputOutputManager;

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
        this.mainMenuScreen = new MainMenuScreen(game); 
        this.introScreen = new IntroScreen(game);
    }

    public MainMenuScreen getMainMenuScreen() { // Add this method to get the MainMenuScreen
        return mainMenuScreen;
    }

    public void showMainScreen() {
        game.setScreen(new MainMenuScreen(game));
    }

    public void showPlayScreen() {
        game.setScreen(new PlayScreen(new SpriteBatch()));
    }

    public void showEndScreen() {
        game.setScreen(new EndScreen(batch, simulationLifeCycle));
        
    }
    
    public void showIntroScreen() {
        game.setScreen(introScreen);
    }
    public void showWinScreen()
    {
    	game.setScreen(new WinScreen(batch, simulationLifeCycle));
    }
    
}
