package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Collectible extends Entity {
    private Texture texture;

    public Collectible(String texturePath, float x, float y, float width, float height) {
        super(null, x, y, width, height);
        texture = new Texture(texturePath);
    }
    
    @Override
    public void update(float deltaTime) {
        // Update logic for collectible (if any)
    }
    
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
}
