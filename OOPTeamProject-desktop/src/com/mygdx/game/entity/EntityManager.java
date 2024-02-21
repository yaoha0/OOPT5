package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class EntityManager {
    private Array<Entity> entities;

    public EntityManager() {
        this.entities = new Array<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.removeValue(entity, true);
    }

    public Array<Entity> getEntities() {
        return new Array<>(entities);
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            entity.update(deltaTime);
        }
    }

    public void renderShape(ShapeRenderer shape) {
        for (Entity entity : entities) {
            entity.render(shape);
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
