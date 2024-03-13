package entity;

public class Platform extends Entity {

    public Platform(String texturePath, float x, float y, float width, float height) {
        super(texturePath, x, y, width, height); // x is 0 to start at the left edge of the screen
    }

    @Override
    public void update(float deltaTime) {
    	
    }
    
    @Override
    public void dispose() {
        texture.dispose();
    }
}
