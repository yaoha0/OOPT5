package entity;

import com.badlogic.gdx.math.Rectangle;

public class Enemy extends Entity {
    private Rectangle boundingBox;
    private float boundingBoxWidth;
    private float boundingBoxHeight;

    public Enemy(String texturePath, float x, float y, float width, float height) {
        super(texturePath, x, y, width, height);
        this.boundingBoxWidth = width;
        this.boundingBoxHeight = height;
        // Initialize the bounding box, can be adjusted if a smaller hitbox is needed
        this.boundingBox = new Rectangle(x, y, boundingBoxWidth, boundingBoxHeight);
    }

    public Rectangle getBounds() {
        // Calculate the dimensions for the smaller bounding box, if needed
        float shrinkAmount = 0.5f; // Adjust shrink amount as necessary
        float shrunkWidth = boundingBoxWidth * (1 - shrinkAmount);
        float shrunkHeight = boundingBoxHeight * (1 - shrinkAmount);
        float xOffset = (boundingBoxWidth - shrunkWidth) / 2; // Center the shrunk box horizontally
        float yOffset = (boundingBoxHeight - shrunkHeight) / 2; // Center the shrunk box vertically

        // Update the bounding box to the smaller, centered version
        boundingBox.set(getX() + xOffset, getY() + yOffset, shrunkWidth, shrunkHeight);
        return boundingBox;
    }

    @Override
    public void update(float deltaTime) {
        // Update logic for the enemy (if any)
    }
}
