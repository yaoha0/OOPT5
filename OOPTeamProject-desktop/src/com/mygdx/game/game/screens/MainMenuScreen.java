package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameMaster;
import engine.scene.ScreenManager;

public class MainMenuScreen implements Screen {

    private ScreenManager screenManager;
    private GameMaster game;
    private Texture backgroundTexture;
    private Texture startButtonTexture;
    private Rectangle startButtonBounds;
    private Texture pauseButtonTexture;
    private Texture titleTexture;

    

    public MainMenuScreen(GameMaster game) {
        this.game = game;
        this.screenManager = game.screenManager;

        // Load textures in the constructor
        backgroundTexture = new Texture("simulationLC/background.png"); // Replace "background.jpg" with the path to your background image
        startButtonTexture = new Texture("simulationLC/start.png");
        pauseButtonTexture = new Texture("simulationLC/video-pause-button.png");
        titleTexture = new Texture("simulationLC/TitleLogo.png");
        
        float buttonWidth = 300;  // or startButtonTexture.getWidth() if you want the exact size
        float buttonHeight = 300; // or startButtonTexture.getHeight()
        float buttonX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        float buttonY = (Gdx.graphics.getHeight() - buttonHeight) / 2 - 80;  // Adjust Y as needed
        startButtonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);

    }
    
    public Rectangle getStartButtonBounds() {
        return startButtonBounds;
    }
    

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(100, 100, 100, 1);

        // Draw background
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(startButtonTexture, ((Gdx.graphics.getWidth() - 300) / 2), (((Gdx.graphics.getHeight() - 300) / 2)-80), 300, 300);
        game.batch.draw(titleTexture, ((Gdx.graphics.getWidth() - titleTexture.getWidth()) / 2), (((Gdx.graphics.getHeight() - titleTexture.getHeight()) / 2)+170), titleTexture.getWidth(),titleTexture.getHeight());
        game.batch.end();


        // Check for input events
       if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if the start button is clicked
            if (x >= (Gdx.graphics.getWidth() - 300) / 2 && x <= (Gdx.graphics.getWidth() - 300) / 2 + 300
                    && y >= (Gdx.graphics.getHeight() - 300) / 2 && y <= (Gdx.graphics.getHeight() - 300) / 2 + 300) {
                // Transition to the play screen
                this.dispose();
                screenManager.showIntroScreen();
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
