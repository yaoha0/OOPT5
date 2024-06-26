package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import engine.scene.ScreenManager;
import engine.simulationLC.SimulationLifeCycle;

public class WinScreen implements Screen {
    private SpriteBatch batch;
    private Texture gameOverTexture;
    private Texture winTexture;
    private Texture playTexture;
    private BitmapFont font;
    private SimulationLifeCycle simulationLifeCycle;
    


    public WinScreen(SpriteBatch batch, SimulationLifeCycle simulationLifeCycle) {
        this.batch = batch;
        this.simulationLifeCycle = simulationLifeCycle;
        gameOverTexture = new Texture("simulationLC/game-over.png");
        winTexture = new Texture("simulationLC/Win.png");
        playTexture = new Texture("simulationLC/play.png");
        font = new BitmapFont();
        font.getData().setScale(2.5f); // Increase the scale of the font
      
    }

    @Override
    public void show() {
        // No need to create resources here since they are created in the constructor
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 1, 0, 1); // Red background color
        float scale = 0.6f;
        float scaledWidth = winTexture.getWidth() * scale;
        float scaledHeight = winTexture.getHeight() * scale;
        batch.begin();
        batch.draw(winTexture, 
                (Gdx.graphics.getWidth() / 2) - (scaledWidth / 2), // Centered horizontally
                ((Gdx.graphics.getHeight() / 2) - (scaledHeight / 2)+100), // Centered vertically
                scaledWidth, 
                scaledHeight);
        batch.draw(playTexture, (Gdx.graphics.getWidth()/2-70), 
                   (Gdx.graphics.getHeight() / 2) - 120,150,150); // Below the game over texture
        font.draw(batch, "Click anywhere to restart\n or press ESC to exit", 
                  Gdx.graphics.getWidth() / 2 - 200, (Gdx.graphics.getHeight() / 2) - 100);
        batch.end();

        // Check for click events to return to the main menu screen
        if (Gdx.input.justTouched()) {
            ScreenManager.getInstance().showMainScreen();
        }

        // Check for "ESC" key press to exit the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        	simulationLifeCycle.exitGame();
        }
    }

    @Override
    public void resize(int width, int height) {
        // This method is not used in this example
    }

    @Override
    public void pause() {
        // This method is not used in this example
    }

    @Override
    public void resume() {
        // This method is not used in this example
    }

    @Override
    public void hide() {
        // This method is not used in this example
    }

    @Override
    public void dispose() {
        // Dispose of resources when the screen is disposed
        batch.dispose();
        gameOverTexture.dispose();
        playTexture.dispose();
        font.dispose();
    }
}
