package entity;


public class Enemy extends Entity{

    public Enemy(String texturePath, float x, float y, float width, float height) {
        super(texturePath, x, y, width, height);
    }
    
    @Override
    public void update(float deltaTime) {
        // Update logic for collectible (if any)
    }
    
}