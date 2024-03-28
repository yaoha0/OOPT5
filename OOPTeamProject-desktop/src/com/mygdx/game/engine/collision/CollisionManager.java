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

public class CollisionManager {
    private ScreenManager screenManager;
    private InputOutputManager inputOutputManager;
    private EntityManager entityManager;
    private PopupManager popupManager;
    private PlayerControlManager controlManager;
    private LevelGenerator levelGenerator;
    private ArrayList<Float> holePositions;
    private ArrayList<Platform> platforms;

    private PlayerBoundaryCollisionHandler boundaryCollisionHandler;
    private PlayerCollectibleCollisionHandler collectibleCollisionHandler;
    private PlayerEnemyCollisionHandler enemyCollisionHandler;
    private PlayerPlatformCollisionHandler platformCollisionHandler;
    private PlayerSpaceshipCollisionHandler spaceshipCollisionHandler;

    public enum CollisionDirection {
        NONE, HORIZONTAL, VERTICAL
    }
    private boolean isOverHole = false;

    public CollisionManager(
            ScreenManager screenManager,
            ArrayList<Float> holePositions,
            ArrayList<Platform> platforms,
            InputOutputManager inputOutputManager,
            EntityManager entityManager,
            PopupManager popupManager,
            PlayerControlManager playerControlManager,
            LevelGenerator levelGenerator
    ) {
        this.screenManager = screenManager;
        this.inputOutputManager = inputOutputManager;
        this.entityManager = entityManager;
        this.popupManager = popupManager;
        this.controlManager = playerControlManager;
        this.levelGenerator = levelGenerator;
        this.holePositions = holePositions;
        this.platforms = platforms;

        // Initialize handlers with necessary dependencies
        this.boundaryCollisionHandler = new PlayerBoundaryCollisionHandler(Gdx.graphics.getWidth());
        this.collectibleCollisionHandler = new PlayerCollectibleCollisionHandler(entityManager, inputOutputManager);
        this.enemyCollisionHandler = new PlayerEnemyCollisionHandler(inputOutputManager, screenManager);
        this.platformCollisionHandler = new PlayerPlatformCollisionHandler(platforms, levelGenerator);
        this.spaceshipCollisionHandler = new PlayerSpaceshipCollisionHandler(entityManager, inputOutputManager, screenManager, popupManager);
    }


    public void updateCollisions(Player player, Enemy enemy, Spaceship spaceship, ArrayList<Platform> platforms, float deltaTime) {
        // always check the players health and handle it if its 0
        checkAndHandlePlayerHealth(player);

        // Delegate boundary collision checking to the handler
        boundaryCollisionHandler.checkBoundaryCollisions(player);


        // Handle player's attempt to collect collectibles
        if (player.isAttemptingToCollect()) {
            for (Entity entity : entityManager.getCollectibles()) {
                Collectible collectible = (Collectible) entity;
                if (collectibleCollisionHandler.checkCollision(player, collectible)) {
                    collectibleCollisionHandler.handleCollision(player, collectible);
                }
            }
            player.setAttemptingToCollect(false); // Reset the flag after attempting to collect
        }

        // Check and handle collision with an enemy
        if (enemyCollisionHandler.checkCollision(player, enemy)) {
            enemyCollisionHandler.handleCollision(player, enemy);
        }

        // Check and handle collision with a spaceship
        if (spaceshipCollisionHandler.checkCollision(player, spaceship)) {
            spaceshipCollisionHandler.handleCollision(player, spaceship);
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

        // Check base ground collision and future collision prediction
        if (checkBaseGroundCollision(player)) {
            handleBaseGroundCollision(player);
        }

        CollisionDirection futureCollisionDirection = checkFutureCollisions(player, deltaTime);
        if (futureCollisionDirection == CollisionDirection.HORIZONTAL) {
            // You might need to handle horizontal collisions here
        } else if (futureCollisionDirection == CollisionDirection.VERTICAL) {
            player.setVelocityY(0); // Stop vertical movement if a vertical collision is predicted
        }

        // Check and handle collisions with platforms
        for (Platform platform : platforms) {
            if (platformCollisionHandler.checkCollision(player, platform)) {
                platformCollisionHandler.handleCollision(player, platform);
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

}
