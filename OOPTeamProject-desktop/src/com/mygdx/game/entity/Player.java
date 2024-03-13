package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
    private float velocityY = 0;
    private boolean isOnGround;
    private boolean isFacingLeft = false;
    private AnimationHandler animationHandler;
    private boolean isWalking, isJumping = false;

    // Method to retrieve the AnimationHandler instance
    public AnimationHandler getAnimationHandler() {
        return this.animationHandler;
    }

    public Player(String idleTexturePath, String walkTexturePath, String jumpTexturePath, int idleFrames, int walkFrames, int jumpFrames, float x, float y, float width, float height) {
        super(idleTexturePath, x, y, width, height);

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
        // Update logic, including simple gravity, should update this.x and this.y from the Entity class

        if (!isOnGround) {
            velocityY -= 9.8 * deltaTime; // Gravity effect
            this.y += velocityY * deltaTime; // Apply movement to this.y of Entity

            if (this.y <= 0) { // Simple ground collision detection
                this.y = 0;
                isOnGround = true;
                velocityY = 0;
            }
        }
    }

    
    // Getters and setters for the Player class should refer to the x and y of the Entity class
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getVelocityY() { return velocityY; }
    public boolean getIsOnGround() { return isOnGround; }
    public boolean getIsWalking () { return isWalking; }
    public boolean getIsJumping () { return isJumping; }
    public boolean getIsFacingLeft () { return isFacingLeft; }
    
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setVelocityY(float velocityY) { this.velocityY = velocityY; }
    public void setOnGround(boolean onGround) { isOnGround = onGround; }
    public void setIsWalking(boolean walking) { isWalking = walking; }
    public void setIsJumping(boolean jumping) { isJumping = jumping; }
    public void setFacingLeft(boolean facingLeft) { isFacingLeft = facingLeft; }
}
