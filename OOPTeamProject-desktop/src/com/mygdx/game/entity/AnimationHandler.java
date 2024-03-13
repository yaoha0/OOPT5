package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationHandler {
    private Animation<TextureRegion> idleAnimation, walkAnimation, jumpAnimation;
    private float stateTime;

    public AnimationHandler(String idlePath, String walkPath, String jumpPath, int idleFrames, int walkFrames, int jumpFrames) {
    	
    	Texture idleSheet = new Texture(idlePath);
    	idleSheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture walkSheet = new Texture(walkPath);
        Texture jumpSheet = new Texture(jumpPath);

        idleAnimation = createAnimation(idleSheet, idleFrames, 1, 0.4f);
        walkAnimation = createAnimation(walkSheet, walkFrames, 1, 0.2f);
        jumpAnimation = createAnimation(jumpSheet, jumpFrames, 1, 0.2f);

        stateTime = 0f;
    }

    private Animation<TextureRegion> createAnimation(Texture sheet, int frameCols, int frameRows, float frameDuration) {
        TextureRegion[][] tmpFrames = TextureRegion.split(sheet,
                sheet.getWidth() / frameCols,
                sheet.getHeight() / frameRows);
        TextureRegion[] animationFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                animationFrames[index++] = tmpFrames[i][j];
            }
        }
        return new Animation<>(frameDuration, animationFrames);
    }

    public TextureRegion getFrame(float deltaTime, boolean isWalking, boolean isJumping, boolean isOnGround) {
        stateTime += deltaTime;

        // Prioritize the jump animation when isJumping is true.
        if (isJumping) {
            return jumpAnimation.getKeyFrame(stateTime, true);
        } else if (isWalking) {
            return walkAnimation.getKeyFrame(stateTime, true);
        } else {
            return idleAnimation.getKeyFrame(stateTime, true);
        }
    }

    public boolean isJumpAnimationFinished() {
        return jumpAnimation.isAnimationFinished(stateTime);
    }

    // Call this method to update stateTime
    public void update(float deltaTime) {
        stateTime += deltaTime; // Increment the state time by the delta time
    }

    // Call this method to reset stateTime when the animation state changes
    public void resetStateTime() {
        stateTime = 0f; // Reset state time
    }

    // Getter for stateTime
    public float getStateTime() {
        return stateTime;
    }
}
