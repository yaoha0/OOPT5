package collision;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entity.*;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {
	private EntityManager entityManager;
    private List<Entity> entities;
    private int Collectiblecount;

    public CollisionManager(EntityManager entityManager) {
    	this.entityManager = entityManager;
    	this.Collectiblecount = 0;
    	entities = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        System.out.println(entities.size());
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
    //checks if any objects are colliding, and handles them appropriately.
    public void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                if (isColliding(entities.get(i), entities.get(j))) {
                    handleCollision(entities.get(j), entities.get(i));
                }
            }
        }
    }
    //Calculates the distance between object1 and object2, and returns T/F depending on collision
    public boolean isColliding(Entity obj1, Entity obj2) {
    	float hitbox = (obj1.getWidth() + obj2.getHeight())/3;
    	double distanceSquared = Math.pow(obj1.getX() - obj2.getX(), 2) + Math.pow(obj1.getY() - obj2.getY(), 2);
        double distance = Math.sqrt(distanceSquared);
        return distance <= hitbox;
    }
    
    
    //ToBeImplemented
    public void handleCollision(Entity obj1, Entity obj2) {
        if ((obj1 instanceof Player && obj2 instanceof Collectible) || (obj1 instanceof Collectible && obj2 instanceof Player)) {
            System.out.println("Player has collided with Collectible");
            entityManager.removeEntity(obj1 instanceof Collectible ? obj1 : obj2); // Remove collectible from EntityManager
            removeEntity(obj1 instanceof Collectible ? obj1 : obj2);
            Collectiblecount += 1;
            System.out.println("Player has collected " + Collectiblecount + " collectibles");
        } else if (obj1 instanceof Player && obj2 instanceof Enemy || obj1 instanceof Enemy && obj2 instanceof Player) {
            System.out.println("Player has collided with Enemy");
        }
    }
    	
    }
