package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameMaster;
import engine.scene.ScreenManager;

public class MainMenuScreen implements Screen {

    private ScreenManager screenManager;
    private GameMaster game;
    private Texture backgroundTexture;
    private Texture startButtonTexture;
    private Texture pauseButtonTexture;
    private Texture titleTexture;

    private static final int START_WIDTH = 300;
    private static final int START_HEIGHT = 300;
    private static final float TITLE_WIDTH = 1024;
    private static final float TITLE_HEIGHT = 261;
    

    public MainMenuScreen(GameMaster game) {
        this.game = game;
        this.screenManager = game.screenManager;

        // Load textures in the constructor
        backgroundTexture = new Texture("simulationLC/background.png"); // Replace "background.jpg" with the path to your background image
        startButtonTexture = new Texture("simulationLC/start.png");
        pauseButtonTexture = new Texture("simulationLC/video-pause-button.png");
        titleTexture = new Texture("simulationLC/title.png");
    }

    @Override
    public void show() {
        // Load resources or initialize variables here if needed
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(100, 100, 100, 1);

        // Draw background
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(startButtonTexture, ((Gdx.graphics.getWidth() - START_WIDTH) / 2), (((Gdx.graphics.getHeight() - START_HEIGHT) / 2)-80), START_WIDTH, START_HEIGHT);
        game.batch.draw(titleTexture, ((Gdx.graphics.getWidth() - TITLE_WIDTH) / 2), (((Gdx.graphics.getHeight() - TITLE_HEIGHT) / 2)+120), TITLE_WIDTH, TITLE_HEIGHT);

        game.batch.end();

        // Draw buttons
//        game.batch.begin();
//        game.batch.draw(startButtonTexture, ((Gdx.graphics.getWidth() - START_WIDTH) / 2), (((Gdx.graphics.getHeight() - START_HEIGHT) / 2)-80), START_WIDTH, START_HEIGHT);
//        game.batch.end();

        // Check for input events
        if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if the start button is clicked
            if (x >= (Gdx.graphics.getWidth() - START_WIDTH) / 2 && x <= (Gdx.graphics.getWidth() - START_WIDTH) / 2 + START_WIDTH
                    && y >= (Gdx.graphics.getHeight() - START_HEIGHT) / 2 && y <= (Gdx.graphics.getHeight() - START_HEIGHT) / 2 + START_HEIGHT) {
                // Transition to the play screen
                this.dispose();
                screenManager.showPlayScreen();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Handle screen resize if needed
    }

    @Override
    public void pause() {
        // Pause logic if needed
    }

    @Override
    public void resume() {
        // Resume logic if needed
    }

    @Override
    public void hide() {
        // Hide logic if needed
    }

    @Override
    public void dispose() {
        // Dispose of resources
        backgroundTexture.dispose();
        startButtonTexture.dispose();
        pauseButtonTexture.dispose();
    }
}
