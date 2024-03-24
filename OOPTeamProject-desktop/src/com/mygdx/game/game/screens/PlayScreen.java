package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import engine.collision.CollisionManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.DesktopLauncher;
import engine.aiControl.PathfindingSystem;
import engine.aiControl.AiControlManager;
import engine.aiControl.DecisionMaking;
import engine.aiControl.DetectionSystem;
import engine.entity.EntityManager;
import engine.entity.LevelGenerator;
import engine.rendering.Camera;
import engine.rendering.CameraManager;
import engine.rendering.GameRenderer;
import engine.scene.ScreenManager;
import engine.simulationLC.Ellipsis;
import engine.simulationLC.PopupManager;
import engine.simulationLC.SimulationLifeCycle;
import game.aiControl.NonControlled;
import engine.ioInput.InputOutputManager;
import game.entity.*;
import game.managers.PlayerControlManager;
import com.mygdx.game.GameMaster;

import java.util.ArrayList;

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
    private ScreenManager screenManager;

    // Class attributes
    private PathfindingSystem pathfindingSystem;
    private DetectionSystem detectionSystem;
    private DecisionMaking decisionMaking;
    private NonControlled nonControlled;
    private LevelGenerator levelGenerator;
    private String[] letters = {"N", "E", "P", "T", "U", "N", "E"};

    private GameRenderer gameRenderer;
    private CameraManager cameraManager;

    private Texture heart;

    private ArrayList<Float> holePositions; // Add this attribute
    private ArrayList<com.mygdx.game.game.entity.Platform> platforms;
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
        camera = new OrthographicCamera();
        camera1 = new Camera(width,height);
        // Set up the camera
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //initialize
        initializeComponents();
        initializeEntities();
        initializeUI();
        initializeManagers();
        initializeCamera();
    }

    @Override
    public void render(float delta) {
        clearScreen();

        batch.begin();
        drawHearts();
        batch.end();

        updateCameraAndRender(delta);
        // Handle input and render PopUp
        popupManager.render();
        handlePopupInput();

        updateGameLogic(delta);
        checkGameProgress();
    }

    private void initializeComponents() {
        font = new BitmapFont();
        font.getData().setScale(3);
        backgroundTexture = new Texture("simulationLC/background2.png");
        shapeRenderer = new ShapeRenderer();
        // Create a static projection matrix for UI elements
        uiMatrix = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        heart = new Texture("entity/objects/heart.png");
        System.out.println("Heart texture loaded");

        // decision making components
        detectionSystem = new DetectionSystem();
        pathfindingSystem = new PathfindingSystem();
        decisionMaking = new DecisionMaking(detectionSystem, pathfindingSystem);
        nonControlled = new NonControlled(pathfindingSystem);
    }

    private void drawHearts() {
        for (int i = 0; i < player.getHealth(); i++) {
            batch.draw(heart, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }
    }

    private void initializeEntities() {
        // entity manager
        entityManager = EntityManager.getInstance();
        // Instantiate game entities
        player = new Player("entity/player/idle.png", "entity/player/walk.png",
                "entity/player/jump2.png", 3, 4, 2,
                100, 0, 150, 150);
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
        levelGenerator = new LevelGenerator(platformWidth, groundPlatformHeight, spaceAboveGroundPlatform,
                levelLength, "entity/letters/", EntityManager.getInstance());
        levelGenerator.createFloor(letters);
        platforms = levelGenerator.getPlatforms();
    }

    private void initializeUI() {
        // Create ellipsis
        ellipsis = new Ellipsis("simulationLC/ellipsis.png", Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 50, 50);
    }

    private void initializeCamera() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraManager = new CameraManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), levelLength, 0.1f, width, height);
        cameraManager.initializeCamera(player);
        gameRenderer = new GameRenderer(batch, camera, uiMatrix, entityManager, backgroundTexture, font, ellipsis, collisionManager, heart, player);
    }
    private void initializeManagers() {
        // simulation lifecycle manager
        simulationLifeCycle = new SimulationLifeCycle(GameMaster.getInstance());
        cameraManager = new CameraManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), levelLength, 0.1f, width, height);

        // scene manager
        ScreenManager screenManager = ScreenManager.getInstance();
        // popUp manager
        popupManager = new PopupManager(batch, simulationLifeCycle, camera1);

        // AI control manager
        aicontrolManager = new AiControlManager(2, 200, decisionMaking, nonControlled);
        playerControlManager = new PlayerControlManager(player,this, collisionManager);

        // I/O manager
        inputOutputManager = new InputOutputManager(player,playerControlManager, popupManager, ellipsis,simulationLifeCycle);
        Gdx.input.setInputProcessor(inputOutputManager);

        // collision manager
        collisionManager = new CollisionManager(screenManager, holePositions, platforms, inputOutputManager, entityManager, popupManager);

        // game render (UI etc)
        gameRenderer = new GameRenderer(batch, camera, uiMatrix, entityManager, backgroundTexture, font, ellipsis, collisionManager, heart, player);
        cameraManager.initializeCamera(player); // Set the initial camera position
        //inputOutputManager.playInGameSound();
    }

    private void clearScreen() {
        // Clear the screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void updateCameraAndRender(float delta) {
        cameraManager.update(player);
        gameRenderer.render(delta, cameraManager.getCamera(), uiMatrix);
    }
    private void handlePopupInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (popupManager.isPopupVisible()) {
                popupManager.resumeGame();
                System.out.println("Game resumed.");
            }
        }
    }

    private void updateGameLogic(float delta) {
        // Update and render game entities
        entityManager.update(Gdx.graphics.getDeltaTime());
        playerControlManager.update(delta);
        // for animations
        player.update(delta);
        // check for all collisions
        collisionManager.updateCollisions(player, enemy, spaceship, platforms, delta);
        aicontrolManager.updateAI(enemy, player);
    }

    private void checkGameProgress() {
        // Check if all collectibles have been collected
        if (collisionManager.getCollectibleCount() == 3) {
            simulationLifeCycle.nextLevel(collisionManager.getCollectibleCount());
        }
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
        shapeRenderer.dispose();
        player.dispose();
        enemy.dispose();
        heart.dispose();
    }

    public Platform[] getPlatforms() {
        return platforms.toArray(new Platform[0]);
    }
}

/* public void renderBounds() {
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
 *
 */
