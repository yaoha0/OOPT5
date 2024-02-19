package com.mygdx.game;
import java.util.ArrayList;
import java.util.List;

public class CollisionManager {
    private List<Entity> entities;

    public CollisionManager() {
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
    	float hitbox = (obj1.getWidth() + obj2.getHeight())/2;
    	double distanceSquared = Math.pow(obj1.getX() - obj2.getX(), 2) + Math.pow(obj1.getY() - obj2.getY(), 2);
        double distance = Math.sqrt(distanceSquared);
        return distance <= hitbox;
    }
    
    
    //ToBeImplemented
    public void handleCollision(Entity obj1, Entity obj2) {	
    	System.out.println(obj1.texture + " has collided with " + obj2.texture);
    	
    }
    
}