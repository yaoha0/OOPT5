package scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aiControl.*;
import collision.CollisionManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.DesktopLauncher;
import entity.*;
import ioInput.InputOutputManager;
import playerControl.PlayerControlManager;
import simulationLC.*;
import com.mygdx.game.GameMaster;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class PlayScreen implements Screen {
    // attributes
    private BitmapFont font;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player player;
    private Enemy enemy;
    private Spaceship spaceship;
    private Collectible collectible;
    private Ellipsis ellipsis;
    private int width, height;
    private Vector3 position = new Vector3();
    private Camera camera1;

    private Texture backgroundTexture;
    // Management attributes
    private SimulationLifeCycle simulationLifeCycle;
    private EntityManager entityManager;
    private CollisionManager collisionManager;
    private AiControlManager aicontrolManager;
    private InputOutputManager inputOutputManager;
    private PlayerControlManager playerControlManager;
    private PopupManager popupManager;

    // Class attributes
    private PathfindingSystem pathfindingSystem;
    private DetectionSystem detectionSystem;
    private DecisionMaking decisionMaking;
    private NonControlled nonControlled;
    private LevelGenerator levelGenerator;
    private String[] letters = {"N", "E", "P", "T", "U", "N", "E"};

    private GameRenderer gameRenderer;

    private ArrayList<Float> holePositions; // Add this attribute
    private ArrayList<Platform> platforms; // Add this to store platform tiles
    private ArrayList<Platform> elevatedPlatforms;
    private ShapeRenderer shapeRenderer;

    private float lastPlatformX;
    private float groundPlatformHeight = 50; // Height of the ground platform
    private float platformWidth = 80; // Width of each platform tile
    private float spaceAboveGroundPlatform = 100; // Space above the ground platform where no elevated platform will be placed
    private Matrix4 uiMatrix;
    private float levelLength = Gdx.graphics.getWidth() * 2.5f;



    public PlayScreen(SpriteBatch batch) {
        this.width = DesktopLauncher.getWidth();
        this.height = DesktopLauncher.getHeight();
        this.batch = batch;
        font = new BitmapFont();
        font.getData().setScale(3);
        backgroundTexture = new Texture("simulationLC/background2.png");
        simulationLifeCycle = new SimulationLifeCycle(GameMaster.getInstance()); // Pass the GameMaster instance to the SimulationLifeCycle constructor
        initialize();
    }

    private void initialize() {
        // initialize
        camera = new OrthographicCamera();
        camera1 = new Camera(width,height);

        // Create a static projection matrix for UI elements
        uiMatrix = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();
        // simulation lifecycle manager
        simulationLifeCycle = new SimulationLifeCycle(GameMaster.getInstance());
        // entity manager
        entityManager = EntityManager.getInstance();
        //entityManager = new EntityManager();
        // scene manager
        ScreenManager screenManager = ScreenManager.getInstance();
        // popUp manager
        popupManager = new PopupManager(batch, simulationLifeCycle, camera);

        // decision making components
        detectionSystem = new DetectionSystem();
        pathfindingSystem = new PathfindingSystem();
        decisionMaking = new DecisionMaking(detectionSystem, pathfindingSystem);
        nonControlled = new NonControlled(pathfindingSystem);

        // Set up the camera
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Instantiate game entities
        player = new Player("entity/player/idle.png", "entity/player/walk.png", "entity/player/jump2.png", 3, 4, 2, 100, 0, 150, 150);
        enemy = new Enemy("entity/objects/rock.png", 600, 0, 80, 100);

        this.holePositions = new ArrayList<Float>();
        this.platforms = new ArrayList<Platform>();
        this.elevatedPlatforms = new ArrayList<Platform>();
        // Add entities to the entity manager
        entityManager.addEntity(player);
        entityManager.addEntity(enemy);

        //spawnCollectibles();
        this.lastPlatformX = 0; // Start from the beginning of the screen

        // level generator
        levelGenerator = new LevelGenerator(platformWidth, groundPlatformHeight, spaceAboveGroundPlatform, levelLength, "entity/letters/", EntityManager.getInstance());
        levelGenerator.createFloor(letters);
        platforms = levelGenerator.getPlatforms();

        // Create ellipsis
        ellipsis = new Ellipsis("simulationLC/ellipsis.png", Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 50, 50);

        // AI control manager
        aicontrolManager = new AiControlManager(2, 200, decisionMaking, nonControlled);
        playerControlManager = new PlayerControlManager(player,this, collisionManager);

        // I/O manager
        inputOutputManager = new InputOutputManager(player,playerControlManager, popupManager, ellipsis);
        Gdx.input.setInputProcessor(inputOutputManager);

        // collision manager
        collisionManager = new CollisionManager(screenManager, holePositions, platforms, inputOutputManager, entityManager);

        // Initialize camera's x position to the player's starting x, but ensuring that it does not show off-screen space
        float initialCameraX = Math.max(player.getX() + player.getWidth() / 2, camera.viewportWidth / 2);

        // Set the camera's y position to focus on the player or the center of the screen if the player is too low
        float initialCameraY = Math.max(player.getY() + player.getHeight() / 2, camera.viewportHeight / 2);

        // Apply the initial camera position
        camera.position.set(initialCameraX, initialCameraY, 0);
        camera.update();

        // game render (UI etc)
        gameRenderer = new GameRenderer(batch, camera, uiMatrix, entityManager, backgroundTexture, font,ellipsis,collisionManager);


    }

    @Override
    public void render(float delta) {
        // Clear the screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Update camera to follow the player when moving past the dead zone
        float deadZoneRight = camera.viewportWidth * 0.25f; // For a dead zone on the right

        // Calculate the camera's right boundary limit
        float cameraRightLimit = levelLength - camera.viewportWidth / 2;

        // Only move camera if the player is beyond the dead zone and the camera is within the level boundary
        if (player.getX() + player.getWidth() / 2 > camera.position.x + deadZoneRight) {
            // Calculate potential new camera position based on player's current position
            float newCameraPositionX = player.getX() + player.getWidth() / 2 - deadZoneRight;
            // Clamp the camera's position to prevent it from showing space past the level end
            camera.position.x = Math.min(newCameraPositionX, cameraRightLimit);
        }

        // Make sure the camera doesn't show space off the left side of the world
        camera.position.x = Math.max(camera.position.x, camera.viewportWidth / 2);

        // Update camera's y position as before
        camera.position.y = Math.max((player.getY() + player.getHeight() / 2), (float) height / 2);

        // Update the camera
        camera.update();
        // Ensures the camera's bottom edge is never below the ground level
        camera.position.set(
                (player.getX() + player.getWidth() / 2),
                Math.max((player.getY() + player.getHeight() / 2), (float) height / 2),
                0
        );

        gameRenderer.render(delta);
        // Handle input and render PopUp
        popupManager.render();

        // Check for Escape key press to resume game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (popupManager.isPopupVisible()) {
                // Only resume if PopUp is visible
                popupManager.resumeGame();
                System.out.println("Game resumed."); // Resume the game
            }
        }

        // Update and render game entities
        entityManager.update(Gdx.graphics.getDeltaTime());
        playerControlManager.update(delta);
        // for animations
        player.update(delta);
        // check for all collisions
        collisionManager.updateCollisions(player, enemy, spaceship, platforms, delta);
        aicontrolManager.updateAI(enemy, player);

        // Check if all collectibles have been collected
        if (collisionManager.getCollectibleCount() == 3) {
            simulationLifeCycle.nextLevel(collisionManager.getCollectibleCount());
        }
    }

    public void renderBounds() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw bounding box for the player
        shapeRenderer.setColor(Color.GREEN);
        Rectangle playerBounds = player.getBounds();
        shapeRenderer.rect(playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);

        // Draw bounding boxes for collectibles
        for (Entity collectible : entityManager.getCollectibles()) {
            shapeRenderer.setColor(Color.YELLOW);
            Rectangle collectibleBounds = collectible.getBounds();
            shapeRenderer.rect(collectibleBounds.x, collectibleBounds.y, collectibleBounds.width, collectibleBounds.height);
        }

        // Draw bounding boxes for platforms or other entities as needed
        shapeRenderer.setColor(Color.RED);
        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();
            shapeRenderer.rect(platformBounds.x, platformBounds.y, platformBounds.width, platformBounds.height);
        }

        shapeRenderer.end();
    }

    public SimulationLifeCycle getSimulationLifeCycle() {
        return simulationLifeCycle;
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
        EntityManager.getInstance().dispose();
        popupManager.dispose();
        ellipsis.dispose();
        shapeRenderer.dispose(); // Dispose of the ShapeRenderer

    }

    public Platform[] getPlatforms() {
        return platforms.toArray(new Platform[0]);
    }
}