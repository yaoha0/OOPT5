package game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import engine.entity.Entity;

public class Platform extends Entity {
    private final Rectangle boundingBox;

    public Platform(String texturePath, float x, float y, float width, float height) {
        super(texturePath, x, y, width, height); // x is 0 to start at the left edge of the screen
        boundingBox = new Rectangle(x, y, width, height); // Initialize the bounding box

    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public Texture getTexture() {
        return texture; // Return the texture of the platform
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight()); // Render the platform texture
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void dispose() {
        texture.dispose();
        super.dispose();
    }
}
