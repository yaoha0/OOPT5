package simulationLC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PopupManager {
    private SpriteBatch batch;
    private Texture pauseButtonTexture;
    private Texture exitButtonTexture;
    private Texture playButtonTexture;
    public boolean isPopupVisible;
    private boolean isPaused;
    private SimulationLifeCycle simulationLifeCycle;

    public PopupManager(SpriteBatch batch, SimulationLifeCycle simulationLifeCycle) {
        this.batch = batch;
        this.simulationLifeCycle = simulationLifeCycle;

        // Load pause and exit button textures
        pauseButtonTexture = new Texture("simulationLC/pause.png");
        exitButtonTexture = new Texture("simulationLC/exit.png");
        playButtonTexture = new Texture("simulationLC/playbtn.png");
    }
    
    public void togglePopupVisibility() {
    	isPopupVisible = !isPopupVisible;
    }
    
    public void toggleGamePause() {
        // Toggle the game's pause state and perform any necessary actions
        isPaused = !isPaused;
        if (isPaused) {
            simulationLifeCycle.pauseGame();
        } else {
            simulationLifeCycle.resumeGame();
            isPopupVisible = false;
        }
    }
    
    public void exitGame() {
    	simulationLifeCycle.exitGame();
    }
    

    public void render() {
        batch.begin();
        // draw the buttons
        if (isPopupVisible) {
            // Draw pause/play button
            float buttonWidth = 50;
            float buttonHeight = 50;
            float buttonSpacing = 40;
            float totalButtonWidth = 2 * buttonWidth + buttonSpacing;
            float buttonsX = (Gdx.graphics.getWidth() - totalButtonWidth) / 2f;
            float buttonY = (Gdx.graphics.getHeight() + buttonHeight) / 2f;

            if (isPaused) {
                batch.draw(playButtonTexture, buttonsX, buttonY, buttonWidth, buttonHeight);
            } else {
                batch.draw(pauseButtonTexture, buttonsX, buttonY, buttonWidth, buttonHeight);
            }

            // Draw exit button
            float exitButtonX = buttonsX + buttonWidth + buttonSpacing;
            batch.draw(exitButtonTexture, exitButtonX, buttonY, buttonWidth, buttonHeight);
        }

        batch.end();
    }
    public void resumeGame() {
        isPaused = false;
        // Additional logic to resume the game
        simulationLifeCycle.resumeGame();
        isPopupVisible = false;
    }

    public boolean isPopupVisible() {
        // so that my playscreen can access
        return isPopupVisible;
    }

    public void dispose() {
        // Dispose the textures when they are no longer needed
        pauseButtonTexture.dispose();
        exitButtonTexture.dispose();
        playButtonTexture.dispose();
    }
}