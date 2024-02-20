package playerControl;

import entity.Player;
import scene.PlayScreen;

public class PlayerControlManager {
	private Player player;
	private PlayScreen playScreen; // Reference to PlayScreen
    private float speed = 2.5f; // Adjust speed as necessary
    private float jumpVelocity = 50.0f; // Adjust jump velocity as necessary
    private boolean isPaused = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private float gravity = -25f; // Negative value for downward gravity
    

    public PlayerControlManager(Player player, PlayScreen playScreen) {
        this.player = player;
        this.playScreen = playScreen;
    }

    // This method will be called when the player needs to move left or right
    public void movePlayerHorizontally(float direction) {
        if (!playScreen.isPaused()) {
            player.setX(player.getX() + direction * speed);
        }
    }

    // This method will be called when the player needs to jump
    public void makePlayerJump() {
        if (!playScreen.isPaused() && player.isOnGround()) {
            // Assuming a simple physics model for jumping
            player.setVelocityY(jumpVelocity);
            player.setOnGround(false); // The player is now in the air
        }
    }

    public void togglePauseMenu() {
        isPaused = !isPaused;
        // Handle pause menu display or state change here
    }
    
    
    public void update(float deltaTime) {
        if (!playScreen.isPaused()) {
            // Move left or right
            if (movingLeft) {
                movePlayerHorizontally(-1.0f);
            } else if (movingRight) {
                movePlayerHorizontally(1.0f);
            }

            // Apply gravity if not on the ground
            if (!player.isOnGround()) {
                float newYVelocity = player.getVelocityY() + gravity * deltaTime;
                player.setVelocityY(newYVelocity);
                player.setY(player.getY() + newYVelocity * deltaTime);
                // Check for landing
                if (player.getY() <= 0) {
                    player.setY(0);
                    player.setOnGround(true);
                    player.setVelocityY(0);
                }
            }
        }
    }

    // Call this method from InputOutputManager when LEFT key is pressed/released
    public void setMovingLeft(boolean moving) {
        this.movingLeft = moving;
    }

    // Call this method from InputOutputManager when RIGHT key is pressed/released
    public void setMovingRight(boolean moving) {
        this.movingRight = moving;
    }

    // Additional methods can be added to handle other player actions
}