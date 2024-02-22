package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import scene.EndScreen;
import scene.PlayScreen;
import scene.ScreenManager;
import simulationLC.SimulationLifeCycle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameMaster extends Game {
	private static GameMaster instance = null;
	
	public SpriteBatch batch;
	public ShapeRenderer shape;
	public ScreenManager screenManager;
	public PlayScreen playScreen;
	public EndScreen endScreen;
	public SimulationLifeCycle simulationLifeCycle;
	
	private GameMaster() {}

	public static GameMaster getInstance() {
		if (instance == null) {
			instance = new GameMaster();
		}
		return instance;
	}
	
	@Override
	public void create() {
	    batch = new SpriteBatch();
	    shape = new ShapeRenderer();
	    simulationLifeCycle = new SimulationLifeCycle(this); // Pass 'this' as the GameMaster instance
	    screenManager = ScreenManager.getInstance();
	    screenManager.initialize(this, batch, simulationLifeCycle); // Make sure MainMenuScreen is initialized inside this method
	    playScreen = new PlayScreen(batch);
	    endScreen = new EndScreen(batch, simulationLifeCycle);
	    // Now that everything is initialized, show the main screen.
	    screenManager.showMainScreen();
	}
    
    
}