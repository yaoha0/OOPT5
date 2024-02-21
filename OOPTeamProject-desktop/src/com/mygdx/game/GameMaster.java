package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import scene.EndScreen;
import scene.PlayScreen;
import scene.ScreenManager;
import simulationLC.SimulationLifeCycle;

public class GameMaster extends Game {
	
	public SpriteBatch batch;
	public ShapeRenderer shape;
	public ScreenManager screenManager;
	public PlayScreen playScreen;
	public EndScreen endScreen;
	public SimulationLifeCycle simulationLifeCycle;
	
    @Override
    public void create() {
    	batch = new SpriteBatch();
    	shape = new ShapeRenderer();
    	screenManager = ScreenManager.getInstance();
    	screenManager.initialize(this);
    	screenManager.showMainScreen();
      	playScreen = new PlayScreen(batch);
    	endScreen = new EndScreen(batch, simulationLifeCycle);
    }
}
