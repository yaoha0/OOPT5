package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class EntityManager {
	private static EntityManager instance; 
    private Array<Entity> entities;

    // Singleton
    public static EntityManager getInstance() { 
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }
    
    private EntityManager() { 
        this.entities = new Array<>();
    }

    public void addEntity(Entity entity) {
        if (!entities.contains(entity, true)) {
            entities.add(entity);
        }
    }

    public void removeEntity(Entity entity) {
        entities.removeValue(entity, true);
    }
    
    public void removeCollectible() {
        Array<Entity> collectiblesToRemove = new Array<>();
        for (Entity entity : entities) {
            if (entity instanceof Collectible) {
                collectiblesToRemove.add(entity);
            }
        }
        entities.removeAll(collectiblesToRemove, true);
    }
    
    public Array<Entity> getEntities() {
        return new Array<>(entities);
    }
    
    public Array<Entity> getCollectibles() {
        Array<Entity> collectibles = new Array<>();
        for (Entity entity : entities) {
            if (entity instanceof Collectible) {
                collectibles.add(entity);
            }
        }
        return collectibles;
    }
    
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            entity.update(deltaTime);
        }
    }
    
    public void renderBatch(SpriteBatch batch) {
        for (Entity entity : entities) {
            entity.render(batch);
        }
    }

    public void dispose() {
        for (Entity entity : entities) {
            entity.dispose();
        }
    }
    
}
