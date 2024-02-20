package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Player extends Entity{
    private Texture texture;
    private float x, y;
    private float velocityY = 0;
    private boolean isOnGround = true;

    public Player(String texturePath, float x, float y, float width, float height) {
        super(null, x, y, width, height);
        texture = new Texture(texturePath);
    }
    
    
    //Constructors, getters, setters
    public float getX() {return x;}
    public void setX(float x) {this.x = x;}
    public float getY() {return y;}
    public void setY(float y) {this.y = y;}
    public float getVelocityY() {return velocityY;}
    public void setVelocityY(float velocityY) {this.velocityY = velocityY;}
    public boolean isOnGround() {return isOnGround;}
    public void setOnGround(boolean onGround) {isOnGround = onGround;}
    
    
    @Override
    public void render() {};
    
    @Override
    public void render(SpriteBatch batch) {
    	batch.begin();
    	batch.draw(texture, x, y, width, height);
    	batch.end();
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }
    
    @Override
    public void update(float deltaTime) {
        // Example update logic, including simple gravity
        if (!isOnGround) {
            velocityY -= 9.8 * deltaTime; // Gravity effect
            y += velocityY * deltaTime; // Apply movement
            if (y <= 0) { // Simple ground collision detection
                y = 0;
                isOnGround = true;
                velocityY = 0;
            }
        }
    }
}

