package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Entity{
    private Texture texture;

    public Enemy(String texturePath, float x, float y, float width, float height) {
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