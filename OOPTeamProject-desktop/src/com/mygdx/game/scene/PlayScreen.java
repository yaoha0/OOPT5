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

public class PlayScreen implements Screen {
    // attributes
    private BitmapFont font;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player player;
    private Enemy enemy;
    private Collectible collectible;
    private Ellipsis ellipsis;
    private int width, height;
    private Vector3 position = new Vector3();
    private Camera camera1;

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

    private ArrayList<Float> holePositions; // Add this attribute
    private ArrayList<Platform> platforms; // Add this to store platform tiles
    private ArrayList<Platform> elevatedPlatforms;
    private ShapeRenderer shapeRenderer;

    private float lastPlatformX;
    float groundPlatformHeight = 50; // Height of the ground platform
    float platformWidth = 80; // Width of each platform tile
    float spaceAboveGroundPlatform = 100; // Space above the ground platform where no elevated platform will be placed

    public PlayScreen(SpriteBatch batch) {
        this.width = DesktopLauncher.getWidth();
        this.height = DesktopLauncher.getHeight();
        this.batch = batch;
        font = new BitmapFont();
        font.getData().setScale(3);
        simulationLifeCycle = new SimulationLifeCycle(GameMaster.getInstance()); // Pass the GameMaster instance to the SimulationLifeCycle constructor
        initialize();
    }

    private void initialize() {
        // initialize
        // initialize
        camera = new OrthographicCamera();
        camera1 = new Camera(width,height);

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
        // Set up the camera
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Instantiate game entities
        player = new Player("entity/player/idle.png", "entity/player/walk.png", "entity/player/jump2.png", 3, 4, 2, 100, 0, 150, 150);
        collectible = new Collectible("entity/objects/gemRed.png", 350, 100, 100, 100);
        enemy = new Enemy("entity/enemy/mon1_sprite.png", 600, 0, 150, 150);

        this.holePositions = new ArrayList<Float>();
        this.platforms = new ArrayList<Platform>();
        this.elevatedPlatforms = new ArrayList<Platform>();
        // Add entities to the entity manager
        entityManager.addEntity(player);
        entityManager.addEntity(enemy);
        spawnCollectibles();
        this.lastPlatformX = 0; // Start from the beginning of the screen
        createFloor();

        // Create ellipsis
        ellipsis = new Ellipsis("simulationLC/ellipsis.png", Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 50, 50);

        // AI control manager
        aicontrolManager = new AiControlManager(2, 200, decisionMaking);
        playerControlManager = new PlayerControlManager(player,this, collisionManager);

        // I/O manager
        inputOutputManager = new InputOutputManager(player,playerControlManager, popupManager, ellipsis);
        Gdx.input.setInputProcessor(inputOutputManager);

        // collision manager
        collisionManager = new CollisionManager(screenManager, holePositions, platforms, inputOutputManager, entityManager);

    }

    @Override
    public void render(float delta) {
        // Clear the screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        camera.position.set(
                (player.getX() + player.getWidth() / 2),
                Math.max((player.getY() + player.getHeight() / 2), (float) height / 2), // Ensures the camera's bottom edge is never below the ground level
                0
        );
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        entityManager.renderBatch(batch);
            // Put ellipsis at top right
            float ellipsisX = ellipsis.getX() + camera.position.x - Gdx.graphics.getWidth() / 2;
            float ellipsisY = ellipsis.getY() + camera.position.y - Gdx.graphics.getHeight() / 2;
            batch.draw(ellipsis.getTexture(), ellipsisX, ellipsisY, ellipsis.getWidth(), ellipsis.getHeight());

            String countNumber = String.valueOf(collisionManager.getCollectibleCount());
            float counterX = 10 + camera.position.x - Gdx.graphics.getWidth() / 2;
            float counterY = Gdx.graphics.getHeight() - 50 - 10 + camera.position.y - Gdx.graphics.getHeight() / 2;
            font.draw(batch, countNumber, counterX, counterY);
            //renderBounds();
        batch.end();

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


        //generateNewPlatformsIfNeeded();
        // Update and render game entities
        entityManager.update(Gdx.graphics.getDeltaTime());
        playerControlManager.update(delta);
        // for animations
        player.update(delta);
        // check for all collisions
        collisionManager.updateCollisions(player, enemy, collectible, platforms, delta);
        aicontrolManager.updateAI(enemy, player);

        // Check if all collectibles have been collected
        if (collisionManager.getCollectibleCount() == 3) {
            simulationLifeCycle.nextLevel(collisionManager.getCollectibleCount());
        }


    }

    public void renderBounds() {
        // Start shape rendering
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Set the color for the player's bounding box
        shapeRenderer.setColor(Color.GREEN);
        Rectangle playerBounds = player.getBounds();
        shapeRenderer.rect(playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);

        Rectangle enemyBounds = enemy.getBounds();
        shapeRenderer.rect(enemyBounds.x, enemyBounds.y, enemyBounds.width, enemyBounds.height);
        // Iterate through platforms to draw their bounding boxes
        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();

            // Set the color for the platform's bounding box
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(platformBounds.x, platformBounds.y, platformBounds.width, platformBounds.height);

            // Check for overlap and draw in a different color
            if (playerBounds.overlaps(platformBounds)) {
                shapeRenderer.setColor(Color.BLUE);
                float overlapX = Math.max(playerBounds.x, platformBounds.x);
                float overlapY = Math.max(playerBounds.y, platformBounds.y);
                float overlapWidth = Math.min(playerBounds.x + playerBounds.width, platformBounds.x + platformBounds.width) - overlapX;
                float overlapHeight = Math.min(playerBounds.y + playerBounds.height, platformBounds.y + platformBounds.height) - overlapY;
                shapeRenderer.rect(overlapX, overlapY, overlapWidth, overlapHeight);
            }
        }

        shapeRenderer.end();
    }
    public void createFloor() {
        this.holePositions.clear();

        float screenWidth = Gdx.graphics.getWidth();


        // Number of initial and final tiles without holes
        int initialSafeTiles = 4;
        int finalSafeTiles = 3;

        // Adjusted for the width of the safe zones at the beginning and the end
        float middleSectionWidth = screenWidth - (initialSafeTiles + finalSafeTiles) * platformWidth;

        //ArrayList<Float> holePositions = new ArrayList<Float>();

        // Create the initial safe ground tiles
        for (float xPosition = 0; xPosition < initialSafeTiles * platformWidth; xPosition += platformWidth) {
            Platform groundPlatform = new Platform("entity/objects/grass.png", xPosition, 0, platformWidth, groundPlatformHeight);
            platforms.add(groundPlatform);
            EntityManager.getInstance().addEntity(groundPlatform);
        }

        // Create ground platforms with holes in the middle section
        for (float xPosition = initialSafeTiles * platformWidth; xPosition < screenWidth - finalSafeTiles * platformWidth; xPosition += platformWidth) {
            if (MathUtils.randomBoolean()) { // Adjust the chance for a hole as needed
                this.holePositions.add(xPosition);
                continue;
            }
            Platform groundPlatform = new Platform("entity/objects/grass.png", xPosition, 0, platformWidth, groundPlatformHeight);
            platforms.add(groundPlatform);
            EntityManager.getInstance().addEntity(groundPlatform);
        }

        // Create the final safe ground tiles
        for (float xPosition = screenWidth - finalSafeTiles * platformWidth; xPosition < screenWidth; xPosition += platformWidth) {
            Platform groundPlatform = new Platform("entity/objects/grass.png", xPosition, 0, platformWidth, groundPlatformHeight);
            platforms.add(groundPlatform);
            EntityManager.getInstance().addEntity(groundPlatform);
        }

        // Create elevated platforms directly above the holes
        for (Float holeX : this.holePositions) {
            float elevationHeight = groundPlatformHeight + spaceAboveGroundPlatform; // Fixed 50 pixels above the ground
            Platform elevatedPlatform = new Platform("entity/objects/grass.png", holeX, elevationHeight, platformWidth, groundPlatformHeight);
            platforms.add(elevatedPlatform); // Add to the list
            elevatedPlatforms.add(elevatedPlatform);
            EntityManager.getInstance().addEntity(elevatedPlatform);
        }

        // Set the lastPlatformX to the end of the final safe zone
        lastPlatformX = screenWidth - finalSafeTiles * platformWidth;
    }

    public void generateNewPlatformsIfNeeded() {
        float rightEdgeOfCamera = camera.position.x + camera.viewportWidth / 2;

        // Continue generating platforms until we fill the space to the right of the camera view
        while (rightEdgeOfCamera > lastPlatformX) {
            // Generate a hole with some probability
            boolean createHole = MathUtils.randomBoolean();
            float platformXPosition = lastPlatformX + (createHole ? 2 * platformWidth : platformWidth);

            if (!createHole) {
                // Add a new platform
                Platform groundPlatform = new Platform("entity/objects/grass.png", platformXPosition, 0, platformWidth, groundPlatformHeight);
                platforms.add(groundPlatform);
                EntityManager.getInstance().addEntity(groundPlatform);

                // Generate elevated platforms as well
                float elevationHeight = groundPlatformHeight + spaceAboveGroundPlatform;
                Platform elevatedPlatform = new Platform("entity/objects/grass.png", platformXPosition, elevationHeight, platformWidth, groundPlatformHeight);
                platforms.add(elevatedPlatform);
                elevatedPlatforms.add(elevatedPlatform);
                EntityManager.getInstance().addEntity(elevatedPlatform);
            } else {
                // Remember the hole position
                this.holePositions.add(platformXPosition);
            }

            // Update the position for the last platform
            lastPlatformX = platformXPosition;
        }
    }
    public void spawnCollectibles() {
        int maxCollectibles = 3; // Maximum number of collectibles including the one on the ground
        float fixedY = 150; // Fixed Y-axis position

        // Clear existing collectibles
        EntityManager.getInstance().removeCollectible();

        // Spawn collectibles randomly along the X-axis at the fixed Y-axis position
        for (int i = 0; i < maxCollectibles; i++) {
            float collectibleX = MathUtils.random(0, Gdx.graphics.getWidth() - collectible.getWidth());
            Collectible collectible = new Collectible("entity/objects/gemRed.png", collectibleX, fixedY, 100, 100);
            entityManager.addEntity(collectible);
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
        entityManager.dispose();
        popupManager.dispose();
        ellipsis.dispose();
        shapeRenderer.dispose(); // Dispose of the ShapeRenderer

    }

    public Platform[] getPlatforms() {
        return platforms.toArray(new Platform[0]);
    }
}
