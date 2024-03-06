package entity;



public class Player extends Entity {
    private float velocityY = 0;
    private boolean isOnGround = true;

    public Player(String texturePath, float x, float y, float width, float height) {
        super(texturePath, x, y, width, height);
    }
    
    @Override
    public void update(float deltaTime) {
        // Update logic, including simple gravity, should update this.x and this.y from the Entity class

        if (!isOnGround) {
            velocityY -= 9.8 * deltaTime; // Gravity effect
            this.y += velocityY * deltaTime; // Apply movement to this.y of Entity

            if (this.y <= 0) { // Simple ground collision detection
                this.y = 0;
                isOnGround = true;
                velocityY = 0;
            }
        }
    }
    
    // Getters and setters for the Player class should refer to the x and y of the Entity class
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getVelocityY() { return velocityY; }
    public boolean isOnGround() { return isOnGround; }
    
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setVelocityY(float velocityY) { this.velocityY = velocityY; }
    public void setOnGround(boolean onGround) { isOnGround = onGround; }
}
