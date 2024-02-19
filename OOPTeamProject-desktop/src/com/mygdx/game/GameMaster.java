package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMaster extends ApplicationAdapter {
    private SpriteBatch batch;
    private EntityManager entityManager;
    private Player player;
    private Enemy enemy;
    private Collectible collectible;
    private Ellipsis ellipsis;
    private Texture pauseButtonTexture;
    private Texture exitButtonTexture;
    private Texture playButtonTexture;
    private boolean isPaused;
    private boolean isPopupVisible;
    private OrthographicCamera camera;
    private SimulationLifeCycle simulationLifeCycle;

    @Override
    public void create() {
        batch = new SpriteBatch();
        entityManager = new EntityManager();

        // Set up the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Instantiate game entities
        player = new Player("entity/player/cat_fighter_sprite0.png", 100, 100, 150, 150);
        collectible = new Collectible("entity/objects/gemRed.png", 350, 100, 100, 100);
        enemy = new Enemy("entity/enemy/mon1_sprite.png", 500, 100, 150, 150);

        // Add entities to the entity manager
        entityManager.addEntity(player);
        entityManager.addEntity(collectible);
        entityManager.addEntity(enemy);

        // Create ellipsis
        ellipsis = new Ellipsis("simulationLC/ellipsis.png", Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 50, 50);

        // Load pause and exit button textures
        pauseButtonTexture = new Texture("simulationLC/pause.png");
        exitButtonTexture = new Texture("simulationLC/exit.png");
        playButtonTexture = new Texture("simulationLC/playbtn.png");

        // Initialize SimulationLifecycle instance
        simulationLifeCycle = new SimulationLifeCycle();
    }

    @Override
    public void render() {
        // Clear the screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Begin drawing
        batch.begin();

        // Draw ellipsis at the top-right corner
        batch.draw(ellipsis.getTexture(), ellipsis.getX(), ellipsis.getY(), ellipsis.getWidth(), ellipsis.getHeight());

        // If ellipsis is clicked, show the popup with pause and exit buttons
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert Y axis
            if (touchX > ellipsis.getX() && touchX < ellipsis.getX() + ellipsis.getWidth() &&
                    touchY > ellipsis.getY() && touchY < ellipsis.getY() + ellipsis.getHeight()) {
                isPopupVisible = !isPopupVisible; // Toggle the popup visibility
            }
        }

// If the popup is visible, render the background and buttons
        if (isPopupVisible) {
            // Draw pause/play button
            float buttonWidth = 50;
            float buttonHeight = 50;
            float buttonSpacing = 40; // Space between buttons
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

            // Check for touch events on pause/play and exit buttons
            if (Gdx.input.justTouched()) {
                float touchX = Gdx.input.getX();
                float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert Y axis
                if (touchX >= buttonsX && touchX <= buttonsX + buttonWidth &&
                        touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                    if (isPaused) {
                        resumeGame(); // Resume the game
                    } else {
                        pauseGame(); // Pause the game
                    }
                } else if (touchX >= exitButtonX && touchX <= exitButtonX + buttonWidth &&
                        touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                    exitGame(); // Exit the game
                }
            }
        }

        batch.end();

        // Update and render game entities
        entityManager.update(Gdx.graphics.getDeltaTime());
        entityManager.renderShape();
        entityManager.renderBatch(batch);

        // Check for Escape key press to resume game
        if (isPopupVisible && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            resumeGame(); // Resume the game
            isPopupVisible = false;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        entityManager.dispose();
        ellipsis.dispose();
        pauseButtonTexture.dispose();
        exitButtonTexture.dispose();
    }

// Function to pause the game
    public void pauseGame() {
        if (!isPopupVisible) return; // Only pause if popup is visible
        // Additional logic to pause the game
        simulationLifeCycle.pauseGame();
        isPaused = true;
        System.out.println("Game paused.");
    }


    // Function to exit the game
    public void exitGame() {
        isPopupVisible = false;
        // Additional logic to exit the game
        simulationLifeCycle.exitGame();
        System.out.println("Game exited.");
    }

    // Function to resume the game
    public void resumeGame() {
        if (!isPopupVisible) return;
        // Only resume if popup is visible
        isPaused = false;
        // Additional logic to resume the game
        simulationLifeCycle.resumeGame();
        System.out.println("Game resumed.");
    }

}

