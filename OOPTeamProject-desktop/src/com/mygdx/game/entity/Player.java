package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
    private float velocityY, velocityX = 0;
    private float lastSafeX, lastSafeY;
    private boolean isOnGround;
    private AnimationHandler animationHandler;
    private boolean isWalking, isJumping, isFacingLeft, hasFallen = false;
    public static final float PIT_LEVEL = -300;


    // Method to retrieve the AnimationHandler instance
    public AnimationHandler getAnimationHandler() {
        return this.animationHandler;
    }

    public Player(String idleTexturePath, String walkTexturePath, String jumpTexturePath, int idleFrames, int walkFrames, int jumpFrames, float x, float y, float width, float height) {
        super(idleTexturePath, x, y, width, height);
        lastSafeX = this.x;
        lastSafeY = this.y;
        // idle, walk, jump, idle frames, walk frames, jump frames,
        animationHandler = new AnimationHandler(idleTexturePath, walkTexturePath, jumpTexturePath, idleFrames, walkFrames, jumpFrames);
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
    
    @Override
    public void update(float deltaTime) {
        System.out.println("Player Y Axis: " + this.y);
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
    }

    // Call this when the player falls through a hole
    public void fall() {
        this.hasFallen = true;
        this.isOnGround = false; // The player is no longer on the ground
        //this.velocityY = 0; // Reset the velocity as the fall starts
    }

    public void land(float platformTopY) {
        this.setY(platformTopY-50); // Set the bottom edge of the player to the top of the platform
        this.setVelocityY(0);    // Stop the vertical movement
        this.setIsOnGround(true);
        this.setIsJumping(false);
    }

    // Method to update the last safe position
    public void updateLastSafePosition() {
        if (this.isOnGround && !this.hasFallen) {
            lastSafeX = this.x;
            lastSafeY = this.y;
        }
    }

    
    // Getters


    public float getVelocityY() { return velocityY; }
    public boolean getIsOnGround() { return isOnGround; }
    public boolean getIsWalking () { return isWalking; }
    public boolean getIsJumping () { return isJumping; }
    public boolean getIsFacingLeft () { return isFacingLeft; }
    public boolean getHasFallen() {return hasFallen;}
    public float getLastSafeX() { return lastSafeX; }
    public float getLastSafeY() { return lastSafeY; }

    //Setters
    public void setVelocityY(float velocityY) { this.velocityY = velocityY; }
    public void setIsOnGround(boolean onGround) { this.isOnGround = onGround; }
    public void setIsWalking(boolean walking) { this.isWalking = walking; }
    public void setIsJumping(boolean jumping) { this.isJumping = jumping; }
    public void setFacingLeft(boolean facingLeft) { this.isFacingLeft = facingLeft; }
    public void setHasFallen(boolean hasFallen) { this.hasFallen = hasFallen; }
    public void setLastSafeY(float lastSafeY) { this.lastSafeY = lastSafeY; }
    public void setLastSafeX(float lastSafeX) { this.lastSafeX = lastSafeX; }

}
