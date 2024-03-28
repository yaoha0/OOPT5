package game.managers;

import game.entity.Player;
import game.screens.PlayScreen;
import engine.collision.CollisionManager;

public class PlayerControlManager {
    private Player player;
    private PlayScreen playScreen;
    private CollisionManager collisionManager;
    private float speed = 200;
    private float jumpVelocity = 500f;
    private float gravity = -770f;
    private boolean isJumpingPressed = false;
    private boolean isMovingRight, isMovingLeft;

    public PlayerControlManager(Player player, PlayScreen playScreen, CollisionManager collisionManager) {
        this.player = player;
        this.playScreen = playScreen;
        this.collisionManager = collisionManager;

        this.player.setIsOnGround(true);
    }

    public void setMovingLeft(boolean moving) {
        isMovingLeft = moving;
        if (isMovingLeft) {
            player.setIsWalking(true);
            player.setVelocityX(-speed);
            player.setIsFacingLeft(true);
        } else if (!isMovingRight) { // Check if the right key is not pressed before stopping
            player.setVelocityX(0);
            player.setIsWalking(false);
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
            player.setIsWalking(false);
        }
        //System.out.println(player.getX());
    }

    public void makePlayerJump() {
        if (player.getIsOnGround() && !isJumpingPressed) { // true
            player.setIsJumping(true);
            player.setVelocityY(jumpVelocity);
            player.setIsOnGround(false);
        }
    }

    public void onJumpKeyReleased() {
        isJumpingPressed = false;
    }

    public void update(float deltaTime) {
        if (!playScreen.getSimulationLifeCycle().isPaused()) {
            applyGravity(deltaTime);

            movePlayerBasedOnVelocity(deltaTime);

            if (player.getIsOnGround() && player.isJumping()) {
                player.setIsJumping(false);
            }
        }
    }

    public void applyGravity(float deltaTime) {
        if (!player.getIsOnGround()) {
            float newVelocityY = player.getVelocityY() + gravity * deltaTime;
            player.setVelocityY(newVelocityY);
        } else {
            player.setVelocityY(0); // Stop applying gravity if on the ground
        }
    }

    private void movePlayerBasedOnVelocity(float deltaTime) {
        player.setX(player.getX() + player.getVelocityX() * deltaTime);
        player.setY(player.getY() + player.getVelocityY() * deltaTime);
    }

    public boolean isMovingRight() { return isMovingRight; }
    public boolean isMovingLeft() { return isMovingLeft; }

    public void setIsMovingLeft(boolean movingLeft) { this.isMovingLeft = movingLeft; }

    public void setIsMovingRight(boolean movingRight) { this.isMovingRight = movingRight; }
}
