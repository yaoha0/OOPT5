package com.mygdx.game;
import java.lang.Thread.State;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	protected float x, y, width, height;
	protected Texture texture;

	public Entity(String texturePath, float x, float y, float width, float height) {
		//this.texture = new Texture(texturePath);
        this.x = x;
    	this.y = y;
        this.width = width;
        this.height = height;
    }
	
	public abstract void update(float deltaTime);
	
    public abstract void render();
    public abstract void render(SpriteBatch batch);
    
    // entity class handles its own dispose
    public void dispose() {
    	if(texture != null) texture.dispose();
    }
    
    // Getter methods
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    
    // Setter methods
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
    public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
    
}
