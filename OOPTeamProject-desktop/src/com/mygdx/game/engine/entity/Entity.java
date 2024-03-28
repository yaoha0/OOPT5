package engine.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {
	protected float x, y, width, height;
	protected Texture texture;

	public Entity(String texturePath, float x, float y, float width, float height) {
	    if (texturePath != null && Gdx.files.internal(texturePath).exists()) {
	        this.texture = new Texture(texturePath);
	    } else {
	        System.out.println("Texture file not found or path is null: " + texturePath);
	    }
        this.x = x;
    	this.y = y;
        this.width = width;
        this.height = height;
    }
	
	public abstract void update(float deltaTime);


	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	public boolean collidesWith(Entity other) {
		return this.getBounds().overlaps(other.getBounds());
	}
	
	public void render(SpriteBatch batch) {
		
	    if (texture != null) {
	        batch.draw(texture, x, y, width, height);
	    }
	}

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
