package scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import aiControl.*;
import collision.CollisionManager;
import entity.*;
import ioInput.InputOutputManager;
import playerControl.PlayerControlManager;
import simulationLC.*;

public class PlayScreen implements Screen {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private EntityManager entityManager;
    private Player player;
    private Enemy enemy;
    private Collectible collectible;
    private Ellipsis ellipsis;
    private OrthographicCamera camera;
    private SimulationLifeCycle simulationLifeCycle;
    private CollisionManager collisionManager;
    private AiControlManager aicontrolManager;
    private PathfindingSystem pathfindingSystem;
    private DetectionSystem detectionSystem;
    private DecisionMaking decisionMaking;
    private PlayerControlManager playerControlManager;
    private InputOutputManager inputOutputManager;
    private PopupManager popupManager;


    public PlayScreen(SpriteBatch batch) {
        this.batch = batch;
        initialize();
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

        // Initialize SimulationLifecycle instance
        simulationLifeCycle = new SimulationLifeCycle();

        // Initialize decision making components
        detectionSystem = new DetectionSystem();
        pathfindingSystem = new PathfindingSystem();
        decisionMaking = new DecisionMaking(detectionSystem, pathfindingSystem);
        // Initialize AI control manager
        aicontrolManager = new AiControlManager(2, 80, decisionMaking);
        playerControlManager = new PlayerControlManager(player,this);
        // Initialize popupManager
        popupManager = new PopupManager(batch, simulationLifeCycle);
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
        // Put ellipsis at top right
        batch.draw(ellipsis.getTexture(), ellipsis.getX(), ellipsis.getY(), ellipsis.getWidth(), ellipsis.getHeight());
        batch.end();

        // Handle input and render popup
        //popupManager.handleInput(ellipsis);
        popupManager.render();

        

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
