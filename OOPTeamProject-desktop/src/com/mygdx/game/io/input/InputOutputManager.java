package io.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import player.control.PlayerControlManager;

public class InputOutputManager implements InputProcessor {
    private PlayerControlManager playerControlManager;

    public InputOutputManager(PlayerControlManager playerControlManager) {
        this.playerControlManager = playerControlManager;
        // Register this class as the input processor
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                // Move player left
            	playerControlManager.setMovingLeft(true);
                break;
            case Input.Keys.RIGHT:
                // Move player right
            	playerControlManager.setMovingRight(true);
                break;
            case Input.Keys.SPACE:
                // Make player jump
                playerControlManager.makePlayerJump();
                break;

        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
    	switch (keycode) {
        case Input.Keys.LEFT:
            playerControlManager.setMovingLeft(false);
            break;
        case Input.Keys.RIGHT:
            playerControlManager.setMovingRight(false);
            break;
    	}
    	return true;
        
    }

    @Override
    public boolean keyTyped(char character) {
        // Not used but must be implemented
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Not used in this context
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Not used
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Not used
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Not used
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Not used
        return false;
    }
    
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}
