package engine.collision;

import engine.entity.Entity;

public interface CollisionHandler {
    boolean checkCollision(Entity entity1, Entity entity2);
    void handleCollision(Entity entity1, Entity entity2);
}
