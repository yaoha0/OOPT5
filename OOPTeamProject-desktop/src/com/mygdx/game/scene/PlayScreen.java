package scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aiControl.*;
import collision.CollisionManager;
import entity.*;
import ioInput.InputOutputManager;
import playerControl.PlayerControlManager;
import simulationLC.*;
import com.mygdx.game.GameMaster;

public class PlayScreen implements Screen {
    // attributes
    private BitmapFont font;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player player;
    private Enemy enemy;
    private Collectible collectible;
    private Ellipsis ellipsis;

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

    public PlayScreen(SpriteBatch batch) {
        this.batch = batch;
        font = new BitmapFont();
        font.getData().setScale(3);
        simulationLifeCycle = new SimulationLifeCycle(GameMaster.getInstance()); // Pass the GameMaster instance to the SimulationLifeCycle constructor
        initialize();
    }

    private void initialize() {
        // initialize
        camera = new OrthographicCamera();

        // simulation lifecycle manager
        simulationLifeCycle = new SimulationLifeCycle(GameMaster.getInstance());

        // entity manager
        entityManager = EntityManager.getInstance();
        //entityManager = new EntityManager();

        // scene manager
        ScreenManager screenManager = ScreenManager.getInstance();

        // collision manager
        collisionManager = new CollisionManager(screenManager, entityManager);

        // popUp manager
        popupManager = new PopupManager(batch, simulationLifeCycle);

        // decision making components
        detectionSystem = new DetectionSystem();
        pathfindingSystem = new PathfindingSystem();
        decisionMaking = new DecisionMaking(detectionSystem, pathfindingSystem);
        // Set up the camera
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Instantiate game entities
        player = new Player("entity/player/cat_fighter_sprite0.png", 100, 0, 150, 150);
        collectible = new Collectible("entity/objects/gemRed.png", 350, 100, 100, 100);
        enemy = new Enemy("entity/enemy/mon1_sprite.png", 600, 0, 150, 150);
        // Add entities to the entity manager
        entityManager.addEntity(player);
        //entityManager.addEntity(collectible);
        entityManager.addEntity(enemy);
        spawnCollectibles();


        // Create ellipsis
        ellipsis = new Ellipsis("simulationLC/ellipsis.png", Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 50, 50);

        // AI control manager
        aicontrolManager = new AiControlManager(2, 200, decisionMaking);
        playerControlManager = new PlayerControlManager(player,this);

        // I/O manager
        inputOutputManager = new InputOutputManager(playerControlManager, popupManager, ellipsis);
        Gdx.input.setInputProcessor(inputOutputManager);


    }

    @Override
    public void render(float delta) {
        // Clear the screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        entityManager.renderBatch(batch);
        // Put ellipsis at top right
        batch.draw(ellipsis.getTexture(), ellipsis.getX(), ellipsis.getY(), ellipsis.getWidth(), ellipsis.getHeight());

        String countNumber = String.valueOf(collisionManager.getCollectibleCount());
        font.draw(batch, countNumber, 10, Gdx.graphics.getHeight() - 50 - 10);
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

        // Update and render game entities
        entityManager.update(Gdx.graphics.getDeltaTime());
        playerControlManager.update(Gdx.graphics.getDeltaTime());
        collisionManager.checkCollisions();
        aicontrolManager.updateAI(enemy, player);

        // Check if all collectibles have been collected
        if (collisionManager.getCollectibleCount() == 3) {
            simulationLifeCycle.nextLevel(collisionManager.getCollectibleCount());
        }
    }

    public void spawnCollectibles() {
        float fixedX = 100; // Fixed x-axis position
        float minX = 0; // Minimum y-axis position
        float maxX = 799; // Maximum y-axis position
        int maxCollectibles = 3; // Maximum number of collectibles

        // Clear existing collectibles
        EntityManager.getInstance().removeCollectible();

        for (int i = 0; i < maxCollectibles; i++) {
            // Generate a random y-axis position within the specified range
            float randomX = (float) (Math.random() * (maxX - minX) + minX);

            // Spawn a collectible at the random position
            //collectible = new Collectible("entity/objects/gemRed.png", 350, 100, 100, 100);
            collectible = new Collectible("entity/objects/gemRed.png", randomX, 100, 100, 100);
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
    }
