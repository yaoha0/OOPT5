package playerControl;

import entity.Player;
import scene.PlayScreen;
import collision.CollisionManager;

public class PlayerControlManager {
    private Player player;
    private PlayScreen playScreen;
    private CollisionManager collisionManager;
    private float speed = 80;
    private float jumpVelocity = 120f;
    private float gravity = -50f;
    private boolean isJumpingPressed = false;

    private boolean isMovingRight, isMovingLeft;

    public PlayerControlManager(Player player, PlayScreen playScreen, CollisionManager collisionManager) {
        this.player = player;
        this.playScreen = playScreen;
        this.collisionManager = collisionManager;
    }

    public void setMovingLeft(boolean moving) {
        isMovingLeft = moving;
        if (isMovingLeft) {
            player.setIsWalking(true);
            player.setVelocityX(-speed);
            player.setIsFacingLeft(true);
        } else if (!isMovingRight) { // Check if the right key is not pressed before stopping
            player.setVelocityX(0);
        }
    }

    public void setMovingRight(boolean moving) {
        isMovingRight = moving;
        if (isMovingRight) {
            player.setIsWalking(true);
            player.setVelocityX(speed);
            player.setIsFacingLeft(false);
        } else if (!isMovingLeft) { // Check if the left key is not pressed before stopping
            player.setVelocityX(0);
        }
        //System.out.println(player.getX());
    }

    public void makePlayerJump() {
        if (player.getIsOnGround() && !isJumpingPressed) { // true
            player.setIsJumping(true);
            player.setVelocityY(jumpVelocity);
            player.setIsOnGround(false);
            isJumpingPressed = true;
        }
    }

    public void onJumpKeyReleased() {
        isJumpingPressed = false;
    }

    public void update(float deltaTime) {
        if (!playScreen.getSimulationLifeCycle().isPaused()) {
            applyGravity(deltaTime);
            movePlayerBasedOnVelocity(deltaTime);
            // Collision checks could be done here or in the PlayScreen's update method

            //System.out.println(player.getIsOnGround() && !player.isJumping());
            // Check if the player has landed and end the jump animation
            if (player.getIsOnGround() && player.isJumping()) {
                player.setIsJumping(false);
            }

        }
    }

    private void applyGravity(float deltaTime) {
        float newVelocityY = player.getVelocityY() + gravity * deltaTime;
        player.setVelocityY(newVelocityY);
    }

    private void movePlayerBasedOnVelocity(float deltaTime) {
        player.setX(player.getX() + player.getVelocityX() * deltaTime);
        player.setY(player.getY() + player.getVelocityY() * deltaTime);
    }

    private void checkForCollisions() {
        // Implement collision detection and handling here
        // This should adjust player's position and velocities as necessary
        // Example:
        //collisionManager.checkCollisions(player);
    }

    public boolean isMovingRight() { return isMovingRight; }
    public boolean isMovingLeft() { return isMovingLeft; }

    public void setIsMovingLeft(boolean movingLeft) { this.isMovingLeft = movingLeft; }

    public void setIsMovingRight(boolean movingRight) { this.isMovingRight = movingRight; }
}
