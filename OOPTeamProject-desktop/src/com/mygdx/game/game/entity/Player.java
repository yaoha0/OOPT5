package game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import engine.entity.AnimationHandler;
import engine.entity.Entity;
import engine.entity.EntityManager;

import java.util.Objects;
import java.util.Random;

public class Player extends Entity {
    private float velocityX = 0;
    private float velocityY = 0;
    private float lastSafeX, lastSafeY;

    private boolean isOnGround = false;
    private boolean isWalking = false;
    private boolean isJumping = false;
    private boolean isFacingLeft = false;
    private boolean isInvulnerable = false;
    private boolean attemptingToCollect = false;

    private int health;
    private float invulnerableTime;
    private float stunTime;

    private String targetWord;
    private StringBuilder collectedLetters = new StringBuilder();
    private String[] possibleTargetWords;

    private float boundingBoxWidth;
    private float boundingBoxHeight;

    private AnimationHandler animationHandler;

    public static final float PIT_LEVEL = -300;


    public Player(String idleTexturePath, String walkTexturePath, String jumpTexturePath, int idleFrames, int walkFrames, int jumpFrames, float x, float y, float width, float height) {
        super(idleTexturePath, x, y, width, height);
        lastSafeX = this.x;
        lastSafeY = this.y;

        // Set the bounding box dimensions (adjust as needed)
        this.boundingBoxWidth = width;
        this.boundingBoxHeight = height;
        this.health = 4; // starting health
        this.isInvulnerable = false;

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

    public boolean collidesWith(Entity entity) {
        // Check if the player's bounding box intersects with the entity's bounding box
        return this.getBounds().overlaps(entity.getBounds());
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
            System.out.println("Health reduced to: " + health);
            if (health > 0) {
                setInvulnerable(true, 2.0f); // 2 seconds of invulnerability
                stunTime = 0.5f; // Stunned for 0.5 seconds
            }
        }
    }

    // Getters
    public float getVelocityX() { return velocityX; }
    public float getVelocityY() { return velocityY; }
    public boolean getIsOnGround() { return isOnGround; }
    public float getLastSafeX() { return lastSafeX; }
    public float getLastSafeY() { return lastSafeY; }
    public TextureRegion getTexture() {
        return animationHandler.getFrame(Gdx.graphics.getDeltaTime(), isWalking, isJumping, isOnGround); // Get the current frame from the animation handler
    }
    public String getCollectedLetters() { return collectedLetters.toString(); }
    public boolean isInvulnerable() { return isInvulnerable; }
    public int getHealth() { return health; }
    public String getTargetWord() { return targetWord; }
    public boolean isAttemptingToCollect() { return attemptingToCollect; }
    public boolean isJumping() { return isJumping; }

    //Setters
    public void setVelocityX(float velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(float velocityY) { this.velocityY = velocityY; }
    public void setIsOnGround(boolean onGround) { this.isOnGround = onGround; }
    public void setIsWalking(boolean walking) { this.isWalking = walking; }
    public void setIsJumping(boolean jumping) { this.isJumping = jumping; }
    public void setIsFacingLeft(boolean facingLeft) { this.isFacingLeft = facingLeft; }
    public void setInvulnerable(boolean isInvulnerable, float duration) {
        this.isInvulnerable = isInvulnerable;
        this.invulnerableTime = duration;
    }

    public void setPossibleTargetWords(String[] words) {
        this.possibleTargetWords = words;
        // Optionally, select a new target word immediately after setting possible words
        Random random = new Random();
        this.targetWord = possibleTargetWords[random.nextInt(possibleTargetWords.length)];
    }
    public void setTargetWord(String word) { this.targetWord = word; }
    public void setAttemptingToCollect(boolean attempting) {
        this.attemptingToCollect = attempting;
    }

}
