package ioInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import entity.Player;
import playerControl.PlayerControlManager;
import simulationLC.PopupManager;
import simulationLC.Ellipsis;

public class InputOutputManager implements InputProcessor {
    private PlayerControlManager playerControlManager;
    private Player player;
    private PopupManager popupManager;
    private Ellipsis ellipsis;
    private Sound collectSound;
    private Sound gameOverSound;

    public InputOutputManager(Player player ,PlayerControlManager playerControlManager, PopupManager popupManager, Ellipsis ellipsis) {
        this.playerControlManager = playerControlManager;
        this.popupManager = popupManager;
        this.ellipsis = ellipsis;
        this.player = player;
        // Register this class as the input processor
        Gdx.input.setInputProcessor(this);
        loadSounds();
    }
    
    public void loadSounds() {
	    collectSound = Gdx.audio.newSound(Gdx.files.internal("ioInput/audio/collectdiamond.mp3"));
	    gameOverSound = Gdx.audio.newSound(Gdx.files.internal("ioInput/audio/gameover.mp3"));
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                System.out.println("keyDown : Left");
                playerControlManager.setMovingLeft(true);
                break;
            case Input.Keys.RIGHT:
                System.out.println("keyDown : RIGHT");
                playerControlManager.setMovingRight(true);
                break;
            case Input.Keys.SPACE:
                System.out.println("Jump Pressed");
                playerControlManager.makePlayerJump();
                break;
            case Input.Keys.ESCAPE:
                if (popupManager.isPopupVisible()) {
                    // Only resume if PopUp is visible
                    popupManager.resumeGame();
                    System.out.println("Game resumed."); // Resume the game
                    break;
                }
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
            case Input.Keys.SPACE:
                playerControlManager.onJumpKeyReleased(); // Handle jump key release
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        // Not used but must be implemented
        return false;
    }

    public boolean isPauseButtonClicked(float x, float y) {
        float buttonWidth = 50;
        float buttonHeight = 50;
        float buttonSpacing = 40; // Space between buttons
        float totalButtonWidth = 2 * buttonWidth + buttonSpacing;
        float buttonsX = (Gdx.graphics.getWidth() - totalButtonWidth) / 2f;
        float buttonY = (Gdx.graphics.getHeight() + buttonHeight) / 2f;

        if (x > buttonsX && x < buttonsX + buttonWidth &&
                y > buttonY && y < buttonY + buttonHeight) {
            return true;//touch inside bound
        }
        return false; //touch outside bound
    }

    public boolean isExitButtonClicked(float x, float y) {
        float buttonWidth = 50;
        float buttonHeight = 50;
        float buttonSpacing = 40; // Space between buttons
        float totalButtonWidth = 2 * buttonWidth + buttonSpacing;
        float buttonsX = (Gdx.graphics.getWidth() - totalButtonWidth) / 2f;
        float buttonY = (Gdx.graphics.getHeight() + buttonHeight) / 2f;
        float exitButtonX = buttonsX + buttonWidth + buttonSpacing;
        if (x > exitButtonX && x < exitButtonX + buttonWidth &&
                y > buttonY && y < buttonY + buttonHeight) {
            return true;//touch inside bound
        }
        return false;//touch outside bound
    }



    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        float touchX = screenX;
        float touchY = Gdx.graphics.getHeight() - screenY;


        Gdx.app.log("TouchDown", "Touch at: " + touchX + ", " + touchY);


        if (touchX >= ellipsis.getX() && touchX <= ellipsis.getX() + ellipsis.getWidth() &&
                touchY >= ellipsis.getY() && touchY <= ellipsis.getY() + ellipsis.getHeight()) {
            popupManager.togglePopupVisibility();
            return true;
        }

        if (popupManager.isPopupVisible()) {
            // Assuming PopupManager has methods like isPauseButtonClicked(touchX, touchY) and isExitButtonClicked(touchX, touchY)
            if (isPauseButtonClicked(touchX, touchY)) {
                popupManager.toggleGamePause(); // This method should handle toggling the pause state and calling simulationLifeCycle methods as needed
            } else if (isExitButtonClicked(touchX, touchY)) {
                popupManager.exitGame(); // Assumes a method in PopupManager that handles game exit
            }
            return true; // Consume the touch event if it's within the PopUp
        }



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
    
    public void playCollectSound() {
        collectSound.play();
    }
    
    public void playGameOverSound() {
        gameOverSound.play();
    }


    /*public void update(float deltaTime) {
        if (player.getIsJumping()) {
            // Apply gravity to vertical velocity
            player.setVelocityY(GRAVITY * deltaTime );

            // Limit maximum falling speed
            player.setVelocityY(MathUtils.clamp(player.getVelocityY(),
                    -MAX_FALL_SPEED, MAX_FALL_SPEED));


            player.setY(player.getY() + player.getVelocityY() * deltaTime);

            // Check if player has landed
            if (player.getY() <= 0) {
                player.setY(0); // Set player on the ground
                player.setIsJumping(false); // Reset jump state
            }
        }
    }*/


}
