package engine.rendering;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import engine.collision.CollisionManager;
import engine.entity.Entity;
import engine.entity.EntityManager;
import engine.simulationLC.Ellipsis;
import game.entity.Platform;
import game.entity.Player;
import game.screens.EndScreen;
import game.screens.PlayScreen;
import engine.scene.ScreenManager;
import engine.simulationLC.SimulationLifeCycle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

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

    public Texture getExclamTexture() {
        return exclamTexture;
    }

    private Texture exclamTexture;
    private ArrayList<Platform> platforms;

    public GameRenderer(SpriteBatch batch, OrthographicCamera camera, Matrix4 uiMatrix, EntityManager entityManager, Texture backgroundTexture, BitmapFont font, Ellipsis ellipsis, CollisionManager collisionManager, Texture heart, Player player, ArrayList<Platform> platforms) {
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
        this.platforms = platforms;
        this.shapeRenderer = new ShapeRenderer();
        this.exclamTexture = new Texture("simulationLC/exclam.jpg");
    }

    public void render(float delta, OrthographicCamera camera, Matrix4 uiMatrix) {
        // Clear the screen, set the camera, begin the batch, draw background, entities, etc.
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //Draw background image
        batch.draw(backgroundTexture, (-Gdx.graphics.getWidth()), (-Gdx.graphics.getHeight()), (Gdx.graphics.getWidth() * 4), (Gdx.graphics.getHeight()) * 4);
        entityManager.renderBatch(batch);
        //renderBounds(); // for debugging purposes

        batch.end();

        // Render UI elements with static projection matrix
        batch.setProjectionMatrix(uiMatrix);
        batch.begin();

        // Draw exclam.png to the left of the ellipsis button
        float padding = 30; // Space between the exclamation mark and the ellipsis button
        float exclamX = ellipsis.getX() - exclamTexture.getWidth() - padding;
        float exclamY = ellipsis.getY();

        // Put ellipsis at top right
        batch.draw(ellipsis.getTexture(), ellipsis.getX(), ellipsis.getY(), ellipsis.getWidth(), ellipsis.getHeight());
        batch.draw(exclamTexture, exclamX, exclamY, ellipsis.getWidth(), ellipsis.getHeight());

        String collectedLetters = player.getCollectedLetters();
        font.draw(batch, "Collected Letters: " + collectedLetters, 10, Gdx.graphics.getHeight() - 50);

        // Draw hearts
        for (int i = 0; i < player.getHealth(); i++) {
            batch.draw(heart, 50 + i * (heart.getWidth() + 10), 50);
        }


        batch.end();

        // Shape rendering if necessary
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);
        // ... (drawing shapes code)
        shapeRenderer.end();
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

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        exclamTexture.dispose();
    }
}
