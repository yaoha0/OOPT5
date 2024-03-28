package engine.collision;

import com.badlogic.gdx.math.Rectangle;
import engine.entity.Entity;
import game.entity.Player;

public class PlayerBoundaryCollisionHandler {
    private static final int LEFT_BOUNDARY = 0;
    private float worldWidth;

    public PlayerBoundaryCollisionHandler(float worldWidth) {
        this.worldWidth = worldWidth; // Assume worldWidth is the right boundary or maximum width of your game world
    }

    public void checkBoundaryCollisions(Player player) {
        Rectangle playerBounds = player.getBounds();

        // Check left boundary
        if (playerBounds.x < LEFT_BOUNDARY) {
            player.setX(LEFT_BOUNDARY); // Position the player right at the left boundary
            if (player.getVelocityX() < 0) {
                player.setVelocityX(0); // Stop the player's leftward movement if they hit the left boundary
            }
        }

        // Check right boundary (end of the level)
        if (playerBounds.x + playerBounds.width > worldWidth) {
            player.setX(worldWidth - playerBounds.width); // Position the player right at the right boundary of the world
            if (player.getVelocityX() > 0) {
                player.setVelocityX(0); // Stop the player's rightward movement if they hit the world's right boundary
            }
        }

        // You can add top and bottom boundary checks here if needed
    }
}
