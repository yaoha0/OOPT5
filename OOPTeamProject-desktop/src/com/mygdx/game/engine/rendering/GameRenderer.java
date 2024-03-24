package engine.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Matrix4;
import engine.collision.CollisionManager;
import engine.entity.EntityManager;
import engine.simulationLC.Ellipsis;
import game.entity.Player;
import game.screens.EndScreen;
import game.screens.PlayScreen;
import engine.scene.ScreenManager;
import engine.simulationLC.SimulationLifeCycle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameRenderer {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private EntityManager entityManager;
    private Texture backgroundTexture;
    private BitmapFont font;
    private Ellipsis ellipsis;
    private Matrix4 uiMatrix;
    private Texture heart;
    private Player player;
    private CollisionManager collisionManager; // Pass a reference to access the collectible count

    public GameRenderer(SpriteBatch batch, OrthographicCamera camera, Matrix4 uiMatrix, EntityManager entityManager, Texture backgroundTexture, BitmapFont font, Ellipsis ellipsis, CollisionManager collisionManager, Texture heart, Player player) {
        this.batch = batch;
        this.camera = camera;
        this.uiMatrix = uiMatrix;
        this.entityManager = entityManager;
        this.backgroundTexture = backgroundTexture;
        this.font = font;
        this.ellipsis = ellipsis;
        this.collisionManager = collisionManager;
        this.heart = heart;
        this.player = player;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(float delta, OrthographicCamera camera, Matrix4 uiMatrix) {
        // Clear the screen, set the camera, begin the batch, draw background, entities, etc.
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //Draw background image
        batch.draw(backgroundTexture, (-Gdx.graphics.getWidth()), (-Gdx.graphics.getHeight()), (Gdx.graphics.getWidth()*4), (Gdx.graphics.getHeight())*4);
        entityManager.renderBatch(batch);
        batch.end();

        // Render UI elements with static projection matrix
        batch.setProjectionMatrix(uiMatrix);
        batch.begin();
        // Put ellipsis at top right
        batch.draw(ellipsis.getTexture(), ellipsis.getX(), ellipsis.getY(), ellipsis.getWidth(), ellipsis.getHeight());
        String collectedLetters = player.getCollectedLetters();
        font.draw(batch, "Collected Letters: " + collectedLetters, 10, Gdx.graphics.getHeight() - 50);

        // Draw hearts
        for (int i = 0; i < player.getHealth(); i++) {
            batch.draw(heart, 50 + i * (heart.getWidth() + 10), 50);
        }

        //renderBounds();
        batch.end();

        // Shape rendering if necessary
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);
        // ... (drawing shapes code)
        shapeRenderer.end();
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}
