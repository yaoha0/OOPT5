package game.entity;

import com.badlogic.gdx.math.Rectangle;
import engine.entity.Entity;

public class Enemy extends Entity {
    private Rectangle boundingBox;
    private float boundingBoxWidth;
    private float boundingBoxHeight;

    public float getLeftBoundary() {
        return leftBoundary;
    }

    public void setLeftBoundary(float leftBoundary) {
        this.leftBoundary = leftBoundary;
    }

    public float getRightBoundary() {
        return rightBoundary;
    }

    public void setRightBoundary(float rightBoundary) {
        this.rightBoundary = rightBoundary;
    }

    private float leftBoundary;
    private float rightBoundary;

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public void setDriftSpeed(float driftSpeed) {
        this.driftspeed = driftSpeed;
    }
    
    public float getDriftSpeed() {
        return driftspeed;
    }

    private float direction; // 1 for right, -1 for left
    private float driftspeed;

    public Enemy(String texturePath, float x, float y, float width, float height, float driftspeed) {
        super(texturePath, x, y, width, height);
        this.boundingBoxWidth = width;
        this.boundingBoxHeight = height;
        // Initialize the bounding box, can be adjusted if a smaller hitbox is needed
        this.boundingBox = new Rectangle(x, y, boundingBoxWidth, boundingBoxHeight);
        this.driftspeed = driftspeed;
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
    // Method to update enemy position, which should be called from the game update loop
    public void update(float deltaTime) {
        // Check for left boundary
        if (getX() < leftBoundary) {
            direction = 1; // Change direction to right
        }
        // Check for right boundary
        else if (getX() > rightBoundary) {
            direction = -1; // Change direction to left
        }

        // Update enemy's X position based on the direction
        setX(getX() + direction * 5 * deltaTime);
    }

    @Override
    public void dispose() {
        super.dispose(); // Call dispose on the superclass to clean up any resources there
    }
}
