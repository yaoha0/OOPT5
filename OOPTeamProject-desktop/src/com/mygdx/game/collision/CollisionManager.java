package collision;

import com.badlogic.gdx.math.Rectangle;
import entity.*;
import scene.ScreenManager;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class CollisionManager {
    private ScreenManager screenManager;
    private int collectibleCount;
    private ArrayList<Float> holePositions;
    private ArrayList<Platform> platforms; // List of all platform tiles
    private InputOutputManager inputOutputManager;

    public CollisionManager(ScreenManager screenManager, ArrayList<Float> holePositions, ArrayList<Platform> platforms, InputOutputManager inputOutputManager) {
        this.screenManager = screenManager;
        this.collectibleCount = 0;
        this.holePositions = holePositions;
        this.platforms = platforms;
        this.inputOutputManager = inputOutputManager;
    }
    
    public int getCollectibleCount() {
        return collectibleCount;
    }

    public void updateCollisions() {
        EntityManager entityManager = EntityManager.getInstance();
        Array<Entity> entities = entityManager.getEntities();
        Entity player = null;

        // Find the player entity
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                player = entity;
                break;
            }
        }

        for (int i = 0; i < entities.size; i++) {
            for (int j = i + 1; j < entities.size; j++) {
                if (isColliding(entities.get(i), entities.get(j))) {
                    handleCollision(entities.get(j), entities.get(i));
                }
            }
        }

        if (player != null) {
            for (Platform platform : platforms) {
                if (isEntityColliding(player, platform)) {
                    Player p = (Player) player;
                    handlePlatformCollision(p, platform);
                    if (p.getVelocityY() < 0 && p.getY() > platform.getY() + platform.getHeight()) {
                        // Correctly position the player on top of the platform
                        p.land(platform.getY() + platform.getHeight());
                        break; // Stop checking for more collisions
                    }
                    break; // Assume one collision at a time for simplicity
                }
            }
            checkPlayerFall(player); // Check for falling off the platforms
        }
    }

    public boolean isEntityColliding(Entity obj1, Entity obj2) {
        Rectangle rect1 = new Rectangle(obj1.getX(), obj1.getY(), obj1.getWidth(), obj1.getHeight());
        Rectangle rect2 = new Rectangle(obj2.getX(), obj2.getY(), obj2.getWidth(), obj2.getHeight());

        System.out.println("Overlaps: " + rect1.overlaps(rect2));
        return rect1.overlaps(rect2);
    }

    public boolean isColliding(Entity obj1, Entity obj2) {
        float hitbox = (obj1.getWidth() + obj2.getHeight()) / 3;
        double distanceSquared = Math.pow(obj1.getX() - obj2.getX(), 2) + Math.pow(obj1.getY() - obj2.getY(), 2);
        double distance = Math.sqrt(distanceSquared);
        return distance <= hitbox;

    }
    
    public void handleCollision(Entity obj1, Entity obj2) {
        if ((obj1 instanceof Player && obj2 instanceof Collectible) || (obj1 instanceof Collectible && obj2 instanceof Player)) {
            EntityManager.getInstance().removeEntity(obj1 instanceof Collectible ? obj1 : obj2); // Remove collectible from EntityManager
            collectibleCount += 1;
            inputOutputManager.playCollectSound();
            //System.out.println("Player has collected " + collectibleCount + " collectibles");
        } else if (obj1 instanceof Player && obj2 instanceof Enemy || obj1 instanceof Enemy && obj2 instanceof Player) {
        	inputOutputManager.playGameOverSound();
        	screenManager.showEndScreen();  
        }
    }

    // Method to handle platform collisions
    private void handlePlatformCollision(Player player, Platform platform) {
        // Get the rectangle for the platform's top surface where the player should land
        Rectangle platformTopRect = new Rectangle(
                platform.getX(), platform.getY() + platform.getHeight() - 1,
                platform.getWidth(), 2 // Small thickness to represent the top surface
        );

        // Get the player's rectangle considering its current movement and position
        Rectangle playerRect = new Rectangle(
                player.getX(), player.getY() + player.getVelocityY(),
                player.getWidth(), player.getHeight()
        );

        // Check if the player is falling and their bottom is colliding with the top surface of the platform
        if (player.getVelocityY() < 0 && playerRect.overlaps(platformTopRect)) {
            player.land(platform.getY() + platform.getHeight());
        }
    }

    private void checkPlayerFall(Entity player) {
        boolean isPlayerOnGround = true; // Initially assume the player is on the ground

        // Assuming player's Y position at 0 means they are on the ground level
        if (player.getY() > 0) {
            isPlayerOnGround = false;
        } else {
            // Check if player is over a hole
            for (Float holeX : holePositions) {
                float playerMiddleX = player.getX() + player.getWidth() / 2;

                if (playerMiddleX > holeX && playerMiddleX < holeX + 50) {
                    // Player is within the horizontal bounds of a hole
                    //System.out.println("Player has fallen.");
                    //System.out.println("Player X: " + player.getX() + " Hole X: " + holeX);
                    ((Player)player).fall();
                    isPlayerOnGround = false;
                    break; // No need to check other holes
                }
            }
        }

        if (isPlayerOnGround) {
            //System.out.println("Player is on the ground.");
        }
    }
}
