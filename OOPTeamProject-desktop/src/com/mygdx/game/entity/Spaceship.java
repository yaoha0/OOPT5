package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Spaceship extends Entity{
    private final Rectangle boundingBox;
    public static final float WIDTH = 100; // Replace with actual width
    public static final float HEIGHT = 50; // Replace with actual height
    private Vector2 position; // Holds the x and y coordinates

    public Spaceship(String texturePath, float x, float y, float width, float height) {
        super(texturePath, x, y, width, height); // x is 0 to start at the left edge of the screen
        boundingBox = new Rectangle(x, y, width, height); // Initialize the bounding box
        position = new Vector2(x, y); // Set the initial position
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

    // Method to set the position of the spaceship
    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    // Method to get the position of the spaceship
    public Vector2 getPosition() {
        return position;
    }
}
