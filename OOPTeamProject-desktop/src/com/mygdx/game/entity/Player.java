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

    private int health;
    private boolean isInvulnerable;
    private float invulnerableTime;

    private float stunTime;
    private String targetWord = "NEPTUNE";
    private StringBuilder collectedLetters = new StringBuilder();

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
        this.health = 3; // starting health
        this.isInvulnerable = false;
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

        if (isInvulnerable) {
            float flashSpeed = 0.25f; // Increase flash frequency
            boolean flashWhite = ((int)((invulnerableTime / flashSpeed)) % 2) == 0;
            if (flashWhite) {
                batch.setColor(1, 1, 1, 1); // Fully white
            } else {
                batch.setColor(0.5f, 0.5f, 0.5f, 1); // Darker shade or the player's normal color
            }
        }

        batch.draw(currentFrame, x, y, width, height);
        // Reset color to normal after drawing the player
        batch.setColor(1, 1, 1, 1);

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
        float shrinkAmount = 0.55f; // 20% shrink towards the middle
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
        if (isInvulnerable) {
            invulnerableTime -= deltaTime;
            if (invulnerableTime <= 0) {
                isInvulnerable = false;
            }
        }

        if (stunTime > 0) {
            stunTime -= deltaTime;
            // Prevent movement logic here, or simply skip movement update
            return; // Skip the rest of the update if stunned
        }
    }

    @Override
    public void dispose() {
        super.dispose(); // Call dispose on the superclass, if it has a dispose method
        if (animationHandler != null) {
            animationHandler.dispose(); // Dispose of the animation handler
        }
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

    public boolean collectLetter(char letter) {
        // Check if the next letter to collect matches the collected letter
        int nextIndex = collectedLetters.length();
        if (nextIndex < targetWord.length() && letter == targetWord.charAt(nextIndex)) {
            collectedLetters.append(letter);
            return true; // Letter was correctly collected
        }
        return false; // Incorrect letter, collection failed
    }

    public boolean hasCollectedAllLetters() {
        return collectedLetters.length() == targetWord.length();
    }

    public void reduceHealth() {
        if (!isInvulnerable) {
            health--;
            if (health > 0) {
                setInvulnerable(true, 2.0f); // 2 seconds of invulnerability
                stunTime = 0.5f; // Stunned for 0.5 seconds
            }
        }

        System.out.println("reduceHealth function: " + health);
    }


    public String getTargetWord() {
        return targetWord;
    }

    public String getCollectedLetters() {
        return collectedLetters.toString();
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

    public void setInvulnerable(boolean isInvulnerable, float duration) {
        this.isInvulnerable = isInvulnerable;
        this.invulnerableTime = duration;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

}
