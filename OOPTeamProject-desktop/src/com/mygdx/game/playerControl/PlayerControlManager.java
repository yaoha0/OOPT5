package playerControl;

import entity.Player;
import scene.PlayScreen;

public class PlayerControlManager {
	private Player player;
	private PlayScreen playScreen; // Reference to PlayScreen
    private float speed = 2.5f; // Adjust speed as necessary
    private float jumpVelocity = 50.0f; // Adjust jump velocity as necessary
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private float gravity = -25f; // Negative value for downward gravity


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
            player.setVelocityY(jumpVelocity);
            player.setOnGround(false);
            player.setIsJumping(true); // This is correct
        }
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

// Apply gravity if not on the ground
            System.out.println(player.getIsOnGround());
            if (!player.getIsOnGround()) {
                float newYVelocity = player.getVelocityY() + gravity * deltaTime;
                player.setVelocityY(newYVelocity);
                player.setY(player.getY() + newYVelocity * deltaTime);
            }

            // Check for landing
            if (player.getY() <= 0) {
                //System.out.println("entered");
                player.setY(0); // Set the player's y position to the ground level
                player.setOnGround(true);
                player.setVelocityY(0);

                // Debug output
                //System.out.println("Animation state time: " + player.getAnimationHandler().getStateTime());
                //System.out.println("Is Jump Animation Finished: " + player.getAnimationHandler().isJumpAnimationFinished());

                // Check if the jump animation is finished
                if (player.getAnimationHandler().isJumpAnimationFinished()) {
                    player.setIsJumping(false);
                }
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
}