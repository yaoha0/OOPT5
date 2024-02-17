package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

public class Player extends Entity{
	private ShapeRenderer shapeRenderer;
	private Color headColor;
	private Color bodyColor;

	// Constructor
    public Player(String texturePath, float x, float y, float width, float height) {
        super(texturePath, x, y, width, height);
        
        shapeRenderer = new ShapeRenderer();
        headColor = Color.RED; // set the color for snake head
        bodyColor = Color.GREEN; // set color for snake body
    }
    
    @Override 
    public void update(float deltaTime) {
    	// add collision
    }
    
    @Override
    public void render() {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN); // Set player color
        shapeRenderer.rect(x, y, width, height); // Render player as a rectangle
        shapeRenderer.end();
    }

    @Override
    public void render(SpriteBatch batch) {
       // render sprite
    }
    
    @Override
    public void dispose() {
        // Dispose of ShapeRenderer
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
    }

    
}

