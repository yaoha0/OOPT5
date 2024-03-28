package engine.collision;

import engine.entity.Entity;
import game.entity.Player;
import game.entity.Platform;
import com.badlogic.gdx.math.Rectangle;
import game.level.LevelGenerator;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerPlatformCollisionHandler implements CollisionHandler {
    private ArrayList<Platform> platforms;
    private LevelGenerator levelGenerator;
    private float tolerance = 0.1f;

    public PlayerPlatformCollisionHandler(ArrayList<Platform> platforms, LevelGenerator levelGenerator) {
        this.platforms = platforms;
        this.levelGenerator = levelGenerator;
    }

    @Override
    public boolean checkCollision(Entity entity1, Entity entity2) {
        if (!(entity1 instanceof Player) || !(entity2 instanceof Platform)) {
            return false;
        }
        Player player = (Player) entity1;
        Platform platform = (Platform) entity2;
        return checkLeftXPlatformCollision(player, platform) ||
                checkRightXPlatformCollision(player, platform) ||
                checkTopYPlatformCollisions(player, platform);
    }

    @Override
    public void handleCollision(Entity entity1, Entity entity2) {
        Player player = (Player) entity1;
        Platform platform = (Platform) entity2;

        if (checkTopYPlatformCollisions(player, platform)) {
            handleYPlatformCollision(player, platform, "top");
        } else if (checkRightXPlatformCollision(player, platform)) {
            handleXPlatformCollisions(player, platform, "right");
        } else if (checkLeftXPlatformCollision(player, platform)) {
            handleXPlatformCollisions(player, platform, "left");
        }
    }




    public boolean checkRightXPlatformCollision(Player player, Platform platform) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platformBounds = platform.getBoundingBox();

        if (playerBounds.overlaps(platformBounds)) {
            float overlapDepthX = Math.min(playerBounds.x + playerBounds.width, platformBounds.x + platformBounds.width)
                    - Math.max(playerBounds.x, platformBounds.x);

            // Handle right collision (player moving right and colliding with the left side of the platform)
            if (player.getVelocityX() > 0 && playerBounds.x + playerBounds.width + tolerance > platformBounds.x && playerBounds.x < platformBounds.x + platformBounds.width) {
                if (overlapDepthX > 0) {
                    return true;
                }
            }
        }
        return false;

    }

    public boolean checkLeftXPlatformCollision(Player player, Platform platform) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platformBounds = platform.getBoundingBox();

        if (playerBounds.overlaps(platformBounds)) {
            float overlapDepthX = Math.min(playerBounds.x + playerBounds.width, platformBounds.x + platformBounds.width)
                    - Math.max(playerBounds.x, platformBounds.x);

            // Handle left collision (player moving left and colliding with the right side of the platform)
            if (player.getVelocityX() < 0 && playerBounds.x - tolerance < platformBounds.x + platformBounds.width && playerBounds.x + playerBounds.width > platformBounds.x) {
                if (overlapDepthX > 0) {
                    return true;
                }
            }
        }
        return false;

    }

    public boolean checkTopYPlatformCollisions(Player player, Platform platform) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platformBounds = platform.getBoundingBox();

        if (playerBounds.overlaps(platformBounds)) {
            //System.out.println("Player overlaps with platform");

            // Calculate overlap depths for both axes
            float overlapDepthY = (playerBounds.y + playerBounds.height) - platformBounds.y;

            // Check for Y-axis collision (on the ground)
            if (player.getVelocityY() <= 0 && overlapDepthY > 0) {
                return true;
            }
        }
        return false;
    }

    public void handleXPlatformCollisions(Player player, Platform platform, String direction) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platformBounds = platform.getBoundingBox();

        if (Objects.equals(direction, "right")) {
            player.setX(platformBounds.x - playerBounds.width - tolerance);
            player.setVelocityX(0);
        } else if (Objects.equals(direction, "left")) {
            player.setX(platformBounds.x + platformBounds.width + tolerance);
            player.setVelocityX(0);
        }

    }

    public void handleYPlatformCollision(Player player, Platform platform, String direction) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platformBounds = platform.getBoundingBox();

        if (Objects.equals(direction, "top")) {
            player.setIsOnGround(true);
            player.setY(platformBounds.y + 9f); // Adjust player's Y position to be on top of the platform
            player.setVelocityY(0); // Stop the vertical movement
        } else if (Objects.equals(direction, "bottom")) {
            // add for bottom
        }
    }

}
