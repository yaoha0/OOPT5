package playerControl;

import entity.Player;
import scene.PlayScreen;

public class PlayerControlManager {
	private Player player;
	private PlayScreen playScreen; // Reference to PlayScreen
    private float speed = 2.5f; // Adjust speed as necessary
    private float jumpVelocity = 70.0f; // Adjust jump velocity as necessary
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private float gravity = -25f; // Negative value for downward gravity
    public static final float PIT_LEVEL = -100;
    public static final float GROUND_LEVEL = 0;
    private boolean isJumpingPressed = false; // Tracks if the jump key is being pressed
    public PlayerControlManager(Player player, PlayScreen playScreen) {
        this.player = player;
        this.playScreen = playScreen;
    }

    // This method will be called when the player needs to move left or right
    public void movePlayerHorizontally(float direction) {
        if (!playScreen.getSimulationLifeCycle().isPaused()) {
            player.setX(player.getX() + direction * speed);
        }
    }

    public void makePlayerJump() {
        if (!playScreen.getSimulationLifeCycle().isPaused() && player.getIsOnGround()) {
            isJumpingPressed = true; // Jump key is pressed
            player.setVelocityY(jumpVelocity);
            player.setIsOnGround(false);
            player.setIsJumping(true);
        }
    }

    // Call this from InputOutputManager when the jump key is released
    public void onJumpKeyReleased() {
        isJumpingPressed = false; // Jump key is released
    }


    public void update(float deltaTime) {
        if (!playScreen.getSimulationLifeCycle().isPaused()) {
            // Move left or right
            if (movingLeft) {
                player.setIsWalking(true);
                movePlayerHorizontally(-1.0f);
            } else if (movingRight) {
                player.setIsWalking(true);
                movePlayerHorizontally(1.0f);
            } else {
                player.setIsWalking(false); // Set to false when not moving
            }


            // Apply gravity every frame if the player is not on the ground or if they have fallen
            if (!player.getIsOnGround() || player.getHasFallen()) {
                float newYVelocity = player.getVelocityY() + gravity * deltaTime; // Apply gravity
                player.setVelocityY(newYVelocity);
                player.setY(player.getY() + newYVelocity * deltaTime); // Update the player's Y position

                // If the jump key is released and the player is moving upwards, cut the jump short
                /*if (!isJumpingPressed && newYVelocity > 0) {
                    player.setVelocityY(newYVelocity * 0.5f); // Apply a factor to reduce the jump height
                }*/
            }

            // Check for landing on the ground, but only if the player has not fallen through a hole
            if (player.getY() <= 0 && !player.getHasFallen()) {
                player.setY(0); // Correct the player's Y position to the ground level
                player.setIsOnGround(true); // The player is on the ground
                player.setVelocityY(0); // Stop the downward velocity

                // If the jump animation is finished, the player is no longer jumping
                if (player.getAnimationHandler().isJumpAnimationFinished()) {
                    player.setIsJumping(false);
                }
            }

            // If the player has fallen, check if they have reached the pit level
            if (player.getHasFallen() && player.getY() <= Player.PIT_LEVEL) {
                player.setY(Player.PIT_LEVEL); // Place the player at the pit level
                player.setHasFallen(false); // The player has finished falling
                // The player is not on the ground if they've fallen into a pit
                player.setIsOnGround(false); // You'll need to set this to true if there's a bottom platform in the pit
                player.setVelocityY(0); // Reset the velocity
            }


        }
    }

    // Call this method from InputOutputManager when LEFT key is pressed/released
    public void setMovingLeft(boolean moving) {
        this.movingLeft = moving;

        // Assume setFacingLeft also flips the animation frames if needed
        if (player != null) {
            player.setFacingLeft(movingLeft);
        }
    }

    // Call this method from InputOutputManager when RIGHT key is pressed/released
    public void setMovingRight(boolean moving) {
        this.movingRight = moving;

        // Flip back to right if movingRight is true and the player was facing left
        if (player != null) {
            if (movingRight && player.getIsFacingLeft()) {
                player.setFacingLeft(false);
            }
        }
    }

    public float getGravity() { return this.gravity; }
}