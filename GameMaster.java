package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameMaster extends ApplicationAdapter {
	private SpriteBatch batch;
    private EntityManager entityManager;
    private Player player;
    private Enemy enemy;
    private Collectible collectible;
    
    @Override
    public void create() {
    	batch = new SpriteBatch();
        entityManager = new EntityManager();

        // texturePath url, x, y, width, height
        player = new Player("entity/player/cat_fighter_sprite0.png", 100, 100, 150, 150); 
        collectible = new Collectible("entity/objects/gemRed.png", 350, 100, 100, 100);
        enemy = new Enemy("entity/enemy/mon1_sprite.png", 500, 100, 150, 150);
        
        entityManager.addEntity(player);
        entityManager.addEntity(collectible);
        entityManager.addEntity(enemy);
    }

    @Override
    public void render() {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        entityManager.update(Gdx.graphics.getDeltaTime());
        
        entityManager.renderShape();
        entityManager.renderBatch(batch);
    }

 
    @Override
    public void dispose() {
        entityManager.dispose();
    }
    
}