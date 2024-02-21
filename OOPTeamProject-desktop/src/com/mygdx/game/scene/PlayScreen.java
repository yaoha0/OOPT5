package scene;

// GDX Libraries imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// Management Packages imports
import aiControl.*;
import collision.CollisionManager;
import entity.*;
import ioInput.InputOutputManager;
import playerControl.PlayerControlManager;
import simulationLC.*;

public class PlayScreen implements Screen {
    // attributes
    private BitmapFont font;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shape;
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
        initialize();
    }

    private void initialize() {
    	// initialize 
        camera = new OrthographicCamera();

        // simulation lifecycle manager
        simulationLifeCycle = new SimulationLifeCycle();

        // entity manager
        entityManager = new EntityManager();

        // scene manager
        ScreenManager screenManager = ScreenManager.getInstance();

        // collision manager
        collisionManager = new CollisionManager(entityManager, screenManager);

        // popUp manager
        popupManager = new PopupManager(batch, simulationLifeCycle);

        // decision making components
        detectionSystem = new DetectionSystem();
        pathfindingSystem = new PathfindingSystem();
        decisionMaking = new DecisionMaking(detectionSystem, pathfindingSystem);

        // AI control manager
        aicontrolManager = new AiControlManager(2, 200, decisionMaking);
        playerControlManager = new PlayerControlManager(player,this);

        // I/O manager
        inputOutputManager = new InputOutputManager(playerControlManager, popupManager, ellipsis);
        Gdx.input.setInputProcessor(inputOutputManager);

        // Set up the camera
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Instantiate game entities
        player = new Player("entity/player/cat_fighter_sprite0.png", 100, 100, 150, 150);
        collectible = new Collectible("entity/objects/gemRed.png", 350, 100, 100, 100);
        enemy = new Enemy("entity/enemy/mon1_sprite.png", 600, 0, 150, 150);
        
        // Create ellipsis
        ellipsis = new Ellipsis("simulationLC/ellipsis.png", Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 50, 50);

        // Add entities to the entity manager
        entityManager.addEntity(player);
        entityManager.addEntity(collectible);
        entityManager.addEntity(enemy);
    }

    @Override
    public void render(float delta) {
        // Clear the screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
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
        entityManager.renderShape(shape);
        entityManager.renderBatch(batch);
        collisionManager.checkCollisions();
        aicontrolManager.updateAI(enemy, player);
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
}
