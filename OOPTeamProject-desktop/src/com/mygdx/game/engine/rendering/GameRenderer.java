package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.screens.EndScreen;
import game.screens.PlayScreen;
import engine.scene.ScreenManager;
import engine.simulationLC.SimulationLifeCycle;

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
	    /* commented out the code below because it was calling initialize twice*/
	    //playScreen = new PlayScreen(batch);
	    //endScreen = new EndScreen(batch, simulationLifeCycle);
	    //tell the SLC to start game, then it calls the mainscreen
	    simulationLifeCycle.startGame(); 
	}
    
    
}
