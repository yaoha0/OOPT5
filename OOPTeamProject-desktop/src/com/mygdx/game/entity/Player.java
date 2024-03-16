package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.lang.reflect.Array;
import java.util.Objects;

public class Player extends Entity {
    private float velocityY, velocityX = 0;
    private float prevX, prevY;
    private float lastSafeX, lastSafeY;
    private boolean isOnGround;
    private boolean isOnPlatform;
    private boolean isOnGap;

    public boolean getHasCollided() {
        return hasCollided;
    }

    public void setHasCollided(boolean hasCollided) {
        this.hasCollided = hasCollided;
    }

    private boolean hasCollided;

    private boolean isCollidingSide;
    private AnimationHandler animationHandler;
    private boolean isWalking, isJumping, isFacingLeft, hasFallen = false;
    public static final float PIT_LEVEL = -300;

    private float boundingBoxWidth;
    private float boundingBoxHeight;
    private Platform currentPlatform;



    private int health = 3;


    // Method to retrieve the AnimationHandler instance
    public AnimationHandler getAnimationHandler() {
        return this.animationHandler;
    }

    public Player(String idleTexturePath, String walkTexturePath, String jumpTexturePath, int idleFrames, int walkFrames, int jumpFrames, float x, float y, float width, float height) {
        super(idleTexturePath, x, y, width, height);
        lastSafeX = this.x;
        lastSafeY = this.y;
        // Set the bounding box dimensions (adjust as needed)
        this.boundingBoxWidth = width;
        this.boundingBoxHeight = height;
        // idle, walk, jump, idle frames, walk frames, jump frames,
        animationHandler = new AnimationHandler(idleTexturePath, walkTexturePath, jumpTexturePath, idleFrames, walkFrames, jumpFrames);
    }

    public TextureRegion getTexture() {
        return animationHandler.getFrame(Gdx.graphics.getDeltaTime(), isWalking, isJumping, isOnGround); // Get the current frame from the animation handler
    }

    public boolean collidesWith(Entity entity) {
        // Check if the player's bounding box intersects with the entity's bounding box
        return this.getBounds().overlaps(entity.getBounds());
    }


    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animationHandler.getFrame(Gdx.graphics.getDeltaTime(),isWalking, isJumping, isOnGround);

        // Check if we need to flip the sprite
        if (isFacingLeft && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!isFacingLeft && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, x, y, width, height);

    }

    public Platform getCurrentPlatform() {
        return currentPlatform;
    }

    public void setCurrentPlatform(Platform currentPlatform) {
        this.currentPlatform = currentPlatform;

    }


    // Add getters for the bounding box dimensions
    public float getBoundingBoxWidth() {
        return boundingBoxWidth;
    }

    public float getBoundingBoxHeight() {
        return boundingBoxHeight;
    }

    public Rectangle getBounds() {
        // Calculate the dimensions for the smaller bounding box
        float shrinkAmount = 0.5f; // 20% shrink towards the middle
        float shrunkWidth = boundingBoxWidth * (1 - shrinkAmount);
        float shrunkHeight = boundingBoxHeight * (1 - shrinkAmount);
        float xOffset = (boundingBoxWidth - shrunkWidth) / 2; // Center the shrunk box
        float yOffset = (boundingBoxHeight - shrunkHeight) / 2; // Center the shrunk box

        //.out.println(shrunkHeight);
        // Create and return the smaller bounding box
        return new Rectangle(getX() + xOffset, getY() + yOffset, shrunkWidth, shrunkHeight);
    }
    
    @Override
    public void update(float deltaTime) {
        this.isOnGround = true;
        //System.out.println("Player Y Axis: " + this.y);
        // If the player has reached the pit level, reset to the last safe position
        if (this.getHasFallen() && this.y <= PIT_LEVEL) {
            // Reset the player's position to the last safe position
            this.x = lastSafeX;
            this.y = lastSafeY;
            this.velocityY = 0; // Reset the velocity
            this.hasFallen = false; // Player has finished falling
            // Depending on your game, you might want to set isOnGround based on whether the last safe position was on the ground
            this.isOnGround = true;
            updateLastSafePosition();
        }
        this.setX(this.getX() + velocityX * deltaTime);
        prevX = x;
        prevY = y;
    }

    // Call this when the player falls through a hole
    public void fall() {
        this.hasFallen = true;
        this.isOnGround = false; // The player is no longer on the ground
        //this.velocityY = 0; // Reset the velocity as the fall starts
    }

    public void land(String type,float platformTopY) {
        if (Objects.equals(type, "platform")) {
            this.setY(platformTopY-50); // Set the bottom edge of the player to the top of the platform
            this.setVelocityY(0);    // Stop the vertical movement
            this.setIsOnGround(true);
            this.setIsJumping(false);
        } else {
            this.setY(0); // Set Y position to the yPosition passed, can be the platform's Y or ground level
            this.setVelocityY(0); // Stop falling
            this.hasFallen = false; // Player has landed and is no longer falling
            this.isOnGround = true; // Player is now on the ground

        }

    }

    // Method to update the last safe position
    public void updateLastSafePosition() {
        if (this.isOnGround && !this.hasFallen) {
            lastSafeX = this.x;
            lastSafeY = this.y;
        }
    }

    
    // Getters
    public float getVelocityX() { return velocityX; }
    public float getVelocityY() { return velocityY; }
    public boolean getIsOnGap() { return isOnGap; }
    public boolean getIsOnPlatform() { return isOnPlatform; }
    public boolean getIsOnGround() { return isOnGround; }
    public boolean getIsWalking () { return isWalking; }
    public boolean isJumping () { return isJumping; }
    public boolean getIsFacingLeft () { return isFacingLeft; }
    public boolean getHasFallen() {return hasFallen;}
    public float getLastSafeX() { return lastSafeX; }
    public float getLastSafeY() { return lastSafeY; }
    public float getPrevX() { return prevX; }
    public float getPrevY() { return prevY; }

    //Setters
    public void setVelocityX(float velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(float velocityY) { this.velocityY = velocityY; }
    public void setIsOnGap(boolean onGap) { this.isOnGap = onGap; }
    public void setIsOnPlatform(boolean onPlatform) { this.isOnPlatform = onPlatform; }
    public void setIsOnGround(boolean onGround) { this.isOnGround = onGround; }
    public void setIsWalking(boolean walking) { this.isWalking = walking; }
    public void setIsJumping(boolean jumping) { this.isJumping = jumping; }
    public void setIsFacingLeft(boolean facingLeft) { this.isFacingLeft = facingLeft; }
    public void setHasFallen(boolean hasFallen) { this.hasFallen = hasFallen; }
    public void setLastSafeY(float lastSafeY) { this.lastSafeY = lastSafeY; }
    public void setLastSafeX(float lastSafeX) { this.lastSafeX = lastSafeX; }
    public void setPrevX(float prevX) { this.prevX = prevX; }
    public void setPrevY(float prevY) { this.prevY = prevY; }

    public boolean isCollidingSide() {
        return isCollidingSide;
    }

    public void setCollidingSide(boolean collidingSide) {
        isCollidingSide = collidingSide;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
