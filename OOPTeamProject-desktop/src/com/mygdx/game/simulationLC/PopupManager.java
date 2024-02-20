package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PopupManager {
    private SpriteBatch batch;
    private Texture pauseButtonTexture;
    private Texture exitButtonTexture;
    private Texture playButtonTexture;
    private boolean isPopupVisible;
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

    public void handleInput(Ellipsis ellipsis) {
        // if ellipsis is clicked, show the popup with pause and exit buttons
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert Y axis
            if (touchX > ellipsis.getX() && touchX < ellipsis.getX() + ellipsis.getWidth() &&
                    touchY > ellipsis.getY() && touchY < ellipsis.getY() + ellipsis.getHeight()) {
                isPopupVisible = !isPopupVisible; // Toggle the popup visibility
                if (isPopupVisible) {
                    //pause the game when the popup is visible
                    simulationLifeCycle.pauseGame();
                } else {
                    //resume game when popup is not there
                    simulationLifeCycle.resumeGame();
                    isPopupVisible = false;
                }
            }

            // If popup is visible and pause or exit button is clicked, perform the corresponding action
            if (isPopupVisible) {
                float buttonWidth = 50;
                float buttonHeight = 50;
                float buttonSpacing = 40; // Space between buttons
                float totalButtonWidth = 2 * buttonWidth + buttonSpacing;
                float buttonsX = (Gdx.graphics.getWidth() - totalButtonWidth) / 2f;
                float buttonY = (Gdx.graphics.getHeight() + buttonHeight) / 2f;
                float exitButtonX = buttonsX + buttonWidth + buttonSpacing;

                //  if pause button is clicked then determine the status
                if (touchX > buttonsX && touchX < buttonsX + buttonWidth &&
                        touchY > buttonY && touchY < buttonY + buttonHeight) {
                    isPaused = !isPaused; // Toggle the paused state
                    if (isPaused) {
                        simulationLifeCycle.pauseGame(); // pause game
                    } else {
                        simulationLifeCycle.resumeGame(); // resume game status
                        isPopupVisible = false;
                    }
                }

                //if exit button is clicked then exit game
                if (touchX > exitButtonX && touchX < exitButtonX + buttonWidth &&
                        touchY > buttonY && touchY < buttonY + buttonHeight) {
                    simulationLifeCycle.exitGame();
                }

            }
        }
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