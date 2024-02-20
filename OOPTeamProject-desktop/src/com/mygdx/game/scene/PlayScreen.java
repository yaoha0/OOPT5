package scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import aiControl.*;
import collision.*;
import entity.*;
import ioInput.*;
import player.control.*;
import simulationLC.*;

public class PlayScreen implements Screen {
    private SpriteBatch batch;
    private ShapeRenderer shape;
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
    private CollisionManager collisionManager;
    private AiControlManager aicontrolManager;
    private PathfindingSystem pathfindingSystem;
    private DetectionSystem detectionSystem;
    private DecisionMaking decisionMaking;
    private PlayerControlManager playerControlManager;
    private InputOutputManager inputOutputManager;


    public PlayScreen(SpriteBatch batch) {
        this.batch = batch;
        initialize();
    }
    public boolean isPaused() {
        return isPaused;
    }

    private void initialize() {
        entityManager = new EntityManager();
        collisionManager = new CollisionManager(entityManager);
        
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

        // Add entities to the collision manager
        collisionManager.addEntity(player);
        collisionManager.addEntity(collectible);
        collisionManager.addEntity(enemy);
        
        // Create ellipsis
        ellipsis = new Ellipsis("simulationLC/ellipsis.png", Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 50, 50);

        // Load pause and exit button textures
        pauseButtonTexture = new Texture("simulationLC/pause.png");
        exitButtonTexture = new Texture("simulationLC/exit.png");
        playButtonTexture = new Texture("simulationLC/playbtn.png");

        // Initialize SimulationLifecycle instance
        simulationLifeCycle = new SimulationLifeCycle();
        
        // Initialize decision making components
        detectionSystem = new DetectionSystem();
        pathfindingSystem = new PathfindingSystem();
        decisionMaking = new DecisionMaking(detectionSystem, pathfindingSystem);
        // Initialize AI control manager
        aicontrolManager = new AiControlManager(2, 400, decisionMaking); 
        playerControlManager = new PlayerControlManager(player,this);
        inputOutputManager = new InputOutputManager(playerControlManager);
        Gdx.input.setInputProcessor(inputOutputManager);
    }

    @Override
    public void render(float delta) {
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
                        // Resume the game
                        if (!isPopupVisible) return;
                        // Only resume if popup is visible
                        isPaused = false;
                        // Additional logic to resume the game
                        simulationLifeCycle.resumeGame();
                        System.out.println("Game resumed.");
                    } else {
                        // Pause the game
                        if (!isPopupVisible) return; // Only pause if popup is visible
                        // Additional logic to pause the game
                        simulationLifeCycle.pauseGame();
                        isPaused = true;
                        System.out.println("Game paused.");
                    }
                } else if (touchX >= exitButtonX && touchX <= exitButtonX + buttonWidth &&
                        touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                    // Exit the game
                    isPopupVisible = false;
                    // Additional logic to exit the game
                    //simulationLifeCycle.exitGame();
                    ScreenManager.getInstance().showEndScreen();
                    System.out.println("Game exited.");
                }
            }
        }

        batch.end();

        
        // Update and render game entities
        entityManager.update(Gdx.graphics.getDeltaTime());
        playerControlManager.update(Gdx.graphics.getDeltaTime());
        entityManager.renderShape(shape);
        entityManager.renderBatch(batch);
        collisionManager.checkCollisions();
        aicontrolManager.updateAI(enemy, player);   
        
        // Check for Escape key press to resume game
        if (isPopupVisible && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!isPopupVisible) return;
            // Only resume if popup is visible
            isPaused = false;
            // Additional logic to resume the game
            simulationLifeCycle.resumeGame();
            System.out.println("Game resumed."); // Resume the game
            isPopupVisible = false;
        }
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}
    

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        entityManager.dispose();
        ellipsis.dispose();
        pauseButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
