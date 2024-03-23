package scene;

import collision.CollisionManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import entity.EntityManager;
import simulationLC.Ellipsis;

public class GameRenderer {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private EntityManager entityManager;
    private Texture backgroundTexture;
    private BitmapFont font;
    private Ellipsis ellipsis;
    private Matrix4 uiMatrix;
    private CollisionManager collisionManager; // Pass a reference to access the collectible count

    public GameRenderer(SpriteBatch batch, OrthographicCamera camera, Matrix4 uiMatrix, EntityManager entityManager, Texture backgroundTexture, BitmapFont font, Ellipsis ellipsis, CollisionManager collisionManager) {
        this.batch = batch;
        this.camera = camera;
        this.uiMatrix = uiMatrix; // You might have a separate camera for UI
        this.entityManager = entityManager;
        this.backgroundTexture = backgroundTexture;
        this.font = font;
        this.ellipsis = ellipsis;
        this.collisionManager = collisionManager;
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
        font.draw(batch, String.valueOf(collisionManager.getCollectibleCount()), 10, Gdx.graphics.getHeight() - 50);
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