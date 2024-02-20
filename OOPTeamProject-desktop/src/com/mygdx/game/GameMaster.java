package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameMaster extends Game {
	
	public SpriteBatch batch;
	public ShapeRenderer shape;
	ScreenManager screenManager;
	
    @Override
    public void create() {
    	batch = new SpriteBatch();
    	shape = new ShapeRenderer();
    	screenManager = ScreenManager.getInstance();
    	screenManager.initialize(this);
    	screenManager.showMainScreen();
    }
}
