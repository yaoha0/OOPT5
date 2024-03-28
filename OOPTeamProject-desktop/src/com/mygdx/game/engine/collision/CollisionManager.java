package engine.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import engine.entity.Entity;
import engine.entity.EntityManager;
import engine.ioInput.InputOutputManager;
import engine.scene.ScreenManager;
import engine.simulationLC.PopupManager;
import game.entity.*;
import game.level.LevelGenerator;
import game.managers.PlayerControlManager;

import java.util.ArrayList;
import java.util.Objects;

public class CollisionManager {
    private ScreenManager screenManager;
    private InputOutputManager inputOutputManager;
    private EntityManager entityManager;
    private PopupManager popupManager;
    private PlayerControlManager controlManager;
    private LevelGenerator levelGenerator;

    private ArrayList<Float> holePositions;
    private ArrayList<Platform> platforms;

    private int collectibleCount;
    private float tolerance = 0.1f;
    private boolean isOverHole = false;

    private static final int LEFT_BOUNDARY = 0;
    private static final int RIGHT_BOUNDARY = Gdx.graphics.getWidth();

    public enum CollisionDirection {
        NONE, HORIZONTAL, VERTICAL
    }
    public CollisionManager(ScreenManager screenManager, ArrayList<Float> holePositions, ArrayList<Platform> platforms, InputOutputManager inputOutputManager, EntityManager entityManager, PopupManager popupManager, PlayerControlManager playerControlManager, LevelGenerator levelGenerator) {
        this.screenManager = screenManager;
        this.collectibleCount = 0;
        this.holePositions = holePositions;
        this.platforms = platforms;
        this.inputOutputManager = inputOutputManager;
        this.entityManager = entityManager;
        this.popupManager = popupManager;
        this.controlManager = playerControlManager;
        this.levelGenerator = levelGenerator;
    }

    public int getCollectibleCount() {
        return collectibleCount;
    }

    /**
     * Checks if two entities intersect using their bounding boxes.
     *
     * @param entity1 The first entity.
     * @param entity2 The second entity.
     * @return true if the entities intersect, false otherwise.
     */
    public boolean checkCollision(Entity entity1, Entity entity2) {
        if (entity1 instanceof Player && entity2 instanceof Player) {
            // Handle specific collision logic for Player vs Player if needed
            return false; // Example: Players do not collide with each other
        } else {
            return entity1.collidesWith(entity2); // Use collidesWith method for collision check
        }
    }

    /**
     * Handles the response to a collision between two entities based on their types.
     * This method can be expanded to include specific collision handling logic
     * depending on the types of entities involved.
     *
     * @param entity1 The first entity involved in the collision.
     * @param entity2 The second entity involved in the collision.
     */
    public void checkResponse(Entity entity1, Entity entity2) {

        // Add specific collision response logic here
        if (entity1 instanceof Player && entity2 instanceof Collectible) {
            for (Entity collectible : entityManager.getCollectibles()) {
                // Check collision between player and collectible
                checkCollectibleCollision((Player) entity1, (Collectible) collectible);
            }
        } else if (entity1 instanceof Player && entity2 instanceof Enemy) {
            if (checkEnemyCollision((Player) entity1, (Enemy) entity2)) {
                if (handleEnemyCollision((Player) entity1)) {
                    inputOutputManager.stopInGameSound();
                    inputOutputManager.playGameOverSound();
                    screenManager.showEndScreen();
                }
            }

        } else if (entity1 instanceof Player && entity2 instanceof Spaceship) {
            checkSpaceshipCollision((Player) entity1, (Spaceship) entity2);
        }
    }


    public void updateCollisions(Player player, Enemy enemy, Spaceship spaceship, ArrayList<Platform> platforms, float deltaTime) {
        // Handle collectible collection attempt

        // always check the players health and handle it if its 0
        checkAndHandlePlayerHealth(player);


        if (player.isAttemptingToCollect()) {
            for (Entity entity : entityManager.getCollectibles()) {
                Collectible collectible = (Collectible) entity;
                if (checkCollectibleCollision(player, collectible)) {
                    handleCollectibleCollision(player, collectible);
                }
            }
            player.setAttemptingToCollect(false); // Reset the flag after attempting to collect
        }

        if (player.getY() > 9.0 && player.getIsOnGround()) {
            player.setIsOnGround(false);
            controlManager.applyGravity(deltaTime);
        }

        if (checkIfOverHole(player)) {
            isOverHole = true;
            // The player is over a hole and not supported by a platform, they should fall.
            player.setIsOnGround(false);
            controlManager.applyGravity(deltaTime);
        } else {
            // Not over a hole, so handle the normal on-ground logic.
            isOverHole = false;
            // Rest of your collision logic...
        }

        checkBoundaryCollisions(player);
        // Check collisions with all collectibles
        /*for (Entity collectible : entityManager.getCollectibles()) {
            checkResponse(player, collectible);
        }*/

        checkResponse(player, enemy);
        for (Entity entity : entityManager.getEntities()) {
            if (entity instanceof Spaceship) {
                checkResponse(player, (Spaceship) entity);
            }
        }

        if (checkBaseGroundCollision(player)) {
            handleBaseGroundCollision(player);
        }

        CollisionDirection futureCollisionDirection = checkFutureCollisions(player, deltaTime);
        if (futureCollisionDirection == CollisionDirection.HORIZONTAL) {
            //player.setVelocityX(0); // Stop horizontal movement if a horizontal collision is predicted
        } else if (futureCollisionDirection == CollisionDirection.VERTICAL) {
            player.setVelocityY(0); // Stop vertical movement if a vertical collision is predicted

        }
        //checkPlatformResponse(player, platforms);
        for (Platform platform : platforms) {
            // First, check collisions in each direction
            boolean isRightXColliding = checkRightXPlatformCollision(player, platform);
            boolean isLeftXColliding = checkLeftXPlatformCollision(player, platform);
            boolean isTopYColliding = checkTopYPlatformCollisions(player, platform);

            // Then, handle collisions based on the checks
            if (isTopYColliding) {
                // Player lands on the top of the platform
                handleYPlatformCollision(player, platform, "top");
            } else if (isRightXColliding) {
                // Collision on the right side of the player
                handleXPlatformCollisions(player, platform, "right");
            } else if (isLeftXColliding) {
                // Collision on the left side of the player
                handleXPlatformCollisions(player, platform, "left");
            }
        }
    }

    public void checkAndHandlePlayerHealth(Player player) {
        // Check if player's health has reached 0
        if (player.getHealth() <= 0) {
            // Trigger game over sequence
            inputOutputManager.playGameOverSound();
            inputOutputManager.stopInGameSound();
            screenManager.showEndScreen();
        }
    }
    public boolean checkIfOverHole(Player player) {
        float playerCenterX = player.getX() + player.getBounds().width / 2;
        float playerWidth = player.getBounds().width;
        float thresholdPercentage = 0.8f; // 80% threshold

        for (Float holeCenterX : levelGenerator.getHolePositions()) {
            float holeWidth = 80; // Adjust this value to the actual width of your holes
            //.out.println("Checking hole at X: " + holeCenterX + " with width: " + holeWidth);

            float leftHoleEdge = holeCenterX - holeWidth / 2;
            float rightHoleEdge = holeCenterX + holeWidth / 2;

            // Calculate the overlap
            float leftOverlap = Math.max(playerCenterX - playerWidth / 2, leftHoleEdge);
            float rightOverlap = Math.min(playerCenterX + playerWidth / 2, rightHoleEdge);
            float overlapWidth = rightOverlap - leftOverlap;

            // Calculate the percentage of the player over the hole
            float overlapPercentage = overlapWidth / playerWidth;

            // Check if the overlap is greater than the threshold percentage of the player's width
            if (overlapWidth > 0 && overlapPercentage >= thresholdPercentage) {
                return true;
            }
        }
        return false;
    }
    public boolean checkBaseGroundCollision(Player player) {
        // Check if player has fallen below the pit level and reset to last safe position
        return player.getY() < Player.PIT_LEVEL;
    }

    public void handleBaseGroundCollision(Player player) {
        player.setX(player.getLastSafeX());
        player.setY(player.getLastSafeY());
        // reduce player health
        player.reduceHealth();
        player.setVelocityY(0); // Stop falling
    }
    public void checkSpaceshipCollision(Player player, Spaceship spaceship) {
        Rectangle playerBounds = player.getBounds();
        Rectangle spaceshipBounds = spaceship.getBounds();

        if (playerBounds.overlaps(spaceshipBounds)) {
            handleSpaceshipCollision(player, spaceship);
        }
    }

    public void handleSpaceshipCollision(Player player, Spaceship spaceship) {
        if (player.hasCollectedAllLetters()) {
            // Remove the spaceship from the screen and the entity manager
            entityManager.removeEntity(spaceship);

            // Trigger game over or level completion
            inputOutputManager.playGameOverSound();
            inputOutputManager.stopInGameSound();
            screenManager.showEndScreen();
        } else {
            // The player has not collected all letters, so they cannot end the game yet
            // Handle this case, maybe show a message or prevent certain actions
            System.out.println(player.getCollectedLetters());
        }
    }

    public boolean checkCollectibleCollision(Player player, Collectible collectible) {
        Rectangle playerBounds = player.getBounds();
        Rectangle collectibleBounds = collectible.getBounds();
        return playerBounds.overlaps(collectibleBounds);
    }

    public void handleCollectibleCollision(Player player, Collectible collectible) {
        char letterToCollect = collectible.getLetter();

        System.out.println("letter to collect: " + letterToCollect);
        System.out.println("player collected: " + player.getCollectedLetters());

        if (player.collectLetter(letterToCollect)) {
            // Correct letter collected
            collectibleCount++;
            inputOutputManager.playCollectSound();
            entityManager.removeEntity(collectible);

            if (player.hasCollectedAllLetters()) {
                // Trigger events for collecting all letters
            }
        } else {
            // Incorrect letter collected, apply damage to the player
            player.reduceHealth();
            inputOutputManager.playCollectSound(); // Consider using a sound for incorrect collection
            entityManager.removeEntity(collectible); // Decide if you want to remove the collectible on incorrect collection
        }
    }

    private boolean checkEnemyCollision(Player player, Enemy enemy) {
        if (player.getBounds().overlaps(enemy.getBounds())) {
            // Handle enemy collision logic
            // For example, reduce player health, trigger animations, or end the game
            return true;
        }

        return false;
    }

    // returns true if player has died
    private boolean handleEnemyCollision(Player player) {
        // Assuming there's a way to check if the player is currently invulnerable
        if (player.isInvulnerable()) {
            return false;
        }


        // Reduce player's health and handle invulnerability and stun within this method
        player.reduceHealth();

        if (player.getHealth() <= 0) {
            System.out.println("Player is dead!");
            return true;
        }

        return false;
    }


    public CollisionDirection checkFutureCollisions(Player player, float deltaTime) {
        Rectangle futureBounds = new Rectangle(player.getBounds());
        futureBounds.x += player.getVelocityX() * deltaTime;
        futureBounds.y += player.getVelocityY() * deltaTime;

        for (Platform platform : platforms) {
            if (futureBounds.overlaps(platform.getBoundingBox())) {
                Rectangle platformBounds = platform.getBoundingBox();

                // Calculate the overlap on each axis
                float overlapDepthX = Math.min(futureBounds.x + futureBounds.width, platformBounds.x + platformBounds.width)
                        - Math.max(futureBounds.x, platformBounds.x);
                float overlapDepthY = Math.min(futureBounds.y + futureBounds.height, platformBounds.y + platformBounds.height)
                        - Math.max(futureBounds.y, platformBounds.y);

                // Determine the direction of the collision based on the overlap depths
                if (overlapDepthX < overlapDepthY) {
                    return CollisionDirection.HORIZONTAL;
                } else {
                    return CollisionDirection.VERTICAL;
                }
            }
        }
        return CollisionDirection.NONE; // No collision detected
    }

    public void checkBoundaryCollisions(Player player) {
        Rectangle playerBounds = player.getBounds();

        // Assume a world width of 10000 units, for example
        final float WORLD_WIDTH = 10000;

        // Check left boundary
        if (playerBounds.x < LEFT_BOUNDARY) {
            player.setX(LEFT_BOUNDARY); // Position the player right at the left boundary
            if (player.getVelocityX() < 0) {
                player.setVelocityX(0); // Stop the player's leftward movement if they hit the left boundary
            }
        }

        // Check right boundary (end of the level)
        if (playerBounds.x + playerBounds.width > WORLD_WIDTH) {
            player.setX(WORLD_WIDTH - playerBounds.width); // Position the player right at the right boundary of the world
            if (player.getVelocityX() > 0) {
                player.setVelocityX(0); // Stop the player's rightward movement if they hit the world's right boundary
            }
        }
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


}


