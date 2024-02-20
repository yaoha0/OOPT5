package scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameMaster;

public class MainMenuScreen implements Screen {
	
	private ScreenManager screenManager;
	GameMaster game;
	Texture startButton;
	Texture pauseButton;
	

	private static final int START_WIDTH = 200;
	private static final int START_HEIGHT = 200;
	
	
	public MainMenuScreen(GameMaster game) {
		this.game = game;
		startButton = new Texture("simulationLC/start.png");
		pauseButton = new Texture("simulationLC/video-pause-button.png");
		this.screenManager = game.screenManager;
		
		
	}
	@Override
	public void show() {
	
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(100, 100, 100, 1);
		game.batch.begin();
		game.batch.draw(startButton, 220, 140, START_WIDTH,START_HEIGHT);
		game.batch.end();
		// Check for input events
        if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            
            // Check if the start button is clicked
            if (x >= 220 && x <= 220 + START_WIDTH && y >= 140 && y <= 140 + START_HEIGHT) {
                // Transition to the play screen
            	this.dispose();
                screenManager.showPlayScreen();
            }
        }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		startButton.dispose();
        pauseButton.dispose();

	}

}
