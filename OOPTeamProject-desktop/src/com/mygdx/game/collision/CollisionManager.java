package collision;

import entity.*;
import scene.ScreenManager;

import com.badlogic.gdx.utils.Array; 

public class CollisionManager {
    private ScreenManager screenManager;
    private int collectibleCount;

    public CollisionManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.collectibleCount = 0;
    }
    
    public int getCollectibleCount() {
        return collectibleCount;
    }

    public void checkCollisions() {
        EntityManager entityManager = EntityManager.getInstance(); // Get singleton instance
        Array<Entity> entities = entityManager.getEntities(); // Retrieve entities from EntityManager
        for (int i = 0; i < entities.size; i++) {
            for (int j = i + 1; j < entities.size; j++) {
                if (isColliding(entities.get(i), entities.get(j))) {
                    handleCollision(entities.get(j), entities.get(i));
                }
            }
        }
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
            //System.out.println("Player has collected " + collectibleCount + " collectibles");
        } else if (obj1 instanceof Player && obj2 instanceof Enemy || obj1 instanceof Enemy && obj2 instanceof Player) {
            screenManager.showEndScreen();
        }
    }

    /*public void handlePlatformCollisions(Player player) {
        boolean onPlatform = false;
        for (Entity e : EntityManager.getInstance().getEntities()) {
            if (e instanceof Platform && isEntityAbovePlatform(player, (Platform) e)) {
                alignEntityWithPlatform(player, (Platform) e);
                onPlatform = true;
                break; // Found a platform, no need to check further
            }
        }
        if (!onPlatform) {
            applyGravityToEntity(player);
        }
    }

    private boolean isEntityAbovePlatform(Player player, Platform platform) {
        // Check if the player's bottom is within the platform's top boundaries
        return player.getY() <= platform.getY() + platform.getHeight() && // bottom of player is at or above top of platform
                player.getX() + player.getWidth() > platform.getX() && // right side of player is past left side of platform
                player.getX() < platform.getX() + platform.getWidth(); // left side of player is before right side of platform
    }

    private void alignEntityWithPlatform(Player player, Platform platform) {
        System.out.println("Platform top: " + (platform.getY() + platform.getHeight()));
        System.out.println("Player bottom before align: " + player.getY());
        //player.setY(0 + platform.getHeight()); // Set the player's Y to be on top of the platform
        System.out.println("Player bottom after align: " + player.getY());
        player.setOnGround(true); // Indicate that the player is on the ground
    }

    private void applyGravityToEntity(Player player) {
        player.setOnGround(false); // Player is not on the ground
        // Apply gravity to the player's Y velocity
        player.setVelocityY(player.getVelocityY() - 9.81f); // gravityValue is a constant representing the gravity acceleration
    }*/
}
