package engine.simulationLC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import engine.rendering.Camera;
import game.aiControl.NonControlled;
import game.logic.RiddleGenerator;
import engine.ioInput.InputOutputManager;


public class PopupManager {
    private SpriteBatch batch;
    private Texture pauseButtonTexture;
    private Texture exitButtonTexture;
    private Texture playButtonTexture;
    private Texture muteButtonTexture;
    private Texture playAudioButtonTexture;
    private Texture questPopupTexture;
    private Texture volumeUpButtonTexture;
    private Texture volumeDownButtonTexture;
    public boolean questPopupVisible = false;
    public boolean isPopupVisible;
    public boolean infoPopupVisible = false;
    private boolean isPaused;
    private boolean isMuted = false;
    private SimulationLifeCycle simulationLifeCycle;
    private NonControlled nonControlled;
    private Camera camera; // Add camera attribute
    private RiddleGenerator riddleGenerator;
    private BitmapFont font;

    public PopupManager(SpriteBatch batch, SimulationLifeCycle simulationLifeCycle, Camera camera,RiddleGenerator riddleGenerator, NonControlled nonControlled) {
        this.batch = batch;
        this.simulationLifeCycle = simulationLifeCycle;
        this.camera = camera; // Assign the camera
        this.riddleGenerator = riddleGenerator;
        this.nonControlled = nonControlled;
        this.font = new BitmapFont(Gdx.files.internal("simulationLC/textfontw.fnt"));
        font.getData().setScale(0.65f);
        font.setColor(1, 1, 1, 1);

        // Load pause and exit button textures
        pauseButtonTexture = new Texture("simulationLC/pause.png");
        exitButtonTexture = new Texture("simulationLC/exit.png");
        playButtonTexture = new Texture("simulationLC/playbtn.png");
        playAudioButtonTexture = new Texture("simulationLC/audio.png");
        muteButtonTexture = new Texture("simulationLC/mutebtn.png");
        questPopupTexture = new Texture("simulationLC/questbg.png");
        volumeUpButtonTexture = new Texture("simulationLC/volume_up.png");
        volumeDownButtonTexture = new Texture("simulationLC/volume_down.png");
    }

    public void togglePopupVisibility() {
        isPopupVisible = !isPopupVisible;
    }

    public void toggleGamePause() {
        // Toggle the game's pause state and perform any necessary actions
        isPaused = !isPaused;
        nonControlled.setPaused(isPaused);
        if (isPaused) {
            simulationLifeCycle.pauseGame();
        } else {
            simulationLifeCycle.resumeGame();
            isPopupVisible = false;
        }
    }

    public void exitGame() {
        simulationLifeCycle.exitGame();
    }


    public void render() {
        batch.setProjectionMatrix(camera.camera.combined); // Set the projection matrix to the camera's combined matrix

        batch.begin();
        // draw the buttons
        if (isPopupVisible) {
            // Draw pause/play button
            float buttonWidth = 50;
            float buttonHeight = 50;
            float buttonSpacing = 40;
            float totalButtonWidth = 2 * buttonWidth + buttonSpacing;
            float buttonsX = (Gdx.graphics.getWidth() - totalButtonWidth) / 2f + camera.camera.position.x - Gdx.graphics.getWidth() / 2f;
            float buttonY = (Gdx.graphics.getHeight() + buttonHeight) / 2f + camera.camera.position.y - Gdx.graphics.getHeight() / 2f;


            batch.draw(playButtonTexture, buttonsX, buttonY, buttonWidth, buttonHeight);


            // Draw exit button
            float exitButtonX = buttonsX + buttonWidth + buttonSpacing;
            batch.draw(exitButtonTexture, exitButtonX, buttonY, buttonWidth, buttonHeight);

            // Draw mute button
            // Draw mute button
            float muteButtonX = exitButtonX + buttonWidth + buttonSpacing;
            if (isMuted) {
                batch.draw(playAudioButtonTexture, muteButtonX, buttonY, buttonWidth, buttonHeight);
            } else {
                batch.draw(muteButtonTexture, muteButtonX, buttonY, buttonWidth, buttonHeight);
            }
                     
            // Volume Down Button
            float volumeDownButtonX = muteButtonX + buttonWidth + buttonSpacing; // Position it next to the Volume Up button
            float volumeDownButtonY = buttonY; // Align vertically with other buttons
            batch.draw(volumeDownButtonTexture, volumeDownButtonX, volumeDownButtonY, buttonWidth, buttonHeight);
            
            float volumeUpButtonX = volumeDownButtonX + buttonWidth + buttonSpacing; // Position it next to the existing buttons
            float volumeUpButtonY = buttonY; // Align vertically with other buttons
            batch.draw(volumeUpButtonTexture, volumeUpButtonX, volumeUpButtonY, buttonWidth, buttonHeight);
            
            float volume = InputOutputManager.getCurrentVolume(); // Get the current volume
            String volumeText = "Volume: " + Math.round(volume * 100) + "%"; // Convert volume to a percentage string

            // Position the volume level text appropriately
            float volumeTextX = ((volumeUpButtonX + volumeDownButtonX) / 2) - 20; // Determine based on your UI layout
            float volumeTextY = volumeDownButtonY + 80; // Determine based on your UI layout

            font.draw(batch, volumeText, volumeTextX, volumeTextY);

        }
        if (questPopupVisible) {
            System.out.println("Drawing quest popup");
            float imageWidth = 533;
            float imageHeight = 421;
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            float imageX = (screenWidth - imageWidth) / 2;
            float imageY = (screenHeight - imageHeight) / 2;

            batch.draw(questPopupTexture, imageX, imageY, imageWidth, imageHeight);

            String riddle = riddleGenerator.getCurrentRiddle();
            GlyphLayout layout = new GlyphLayout(font, riddle);
            float textX = imageX + (imageWidth - layout.width) / 2;
            float textY = imageY + (imageHeight + layout.height) / 2;

            font.draw(batch, riddle, textX, textY);
        }

        if (infoPopupVisible) {
            float imageWidth = 533;
            float imageHeight = 421;
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            float imageX = (screenWidth - imageWidth) / 2;
            float imageY = (screenHeight - imageHeight) / 2;

            // Draw the information popup image
            batch.draw(questPopupTexture, imageX, imageY, imageWidth, imageHeight);
            
            String text = "Player Has not collected all Letters";
            GlyphLayout layout = new GlyphLayout(font, text);

            // Calculate the position to center the text on the popup
            float textX = imageX + (imageWidth - layout.width) / 2;
            float textY = imageY + (imageHeight + layout.height) / 2;

            // Draw the text
            font.draw(batch, text, textX, textY);
        }
        batch.end();
    }

    public void resumeGame() {
        isPaused = false;
        // Additional logic to resume the game
        simulationLifeCycle.resumeGame();
        isPopupVisible = false;
    }

    public void toggleMute() {
        isMuted = !isMuted;
        System.out.println("Mute state changed. Now isMuted = " + isMuted);
    }

    public boolean isMuted() {
        return isMuted;
    }

    public boolean isPopupVisible() {
        // so that my PlayScreen can access
        return isPopupVisible;
    }

    public void showQuestPopup() {
        riddleGenerator.startNewRiddle(); // Generate a new riddle
        questPopupVisible = true;
        simulationLifeCycle.pauseGame();
        System.out.println("showQuestPopup appeared liao ");
        System.out.println("Current riddle: " + riddleGenerator.getCurrentRiddle());
    }
    public void hideQuestPopup() {
        questPopupVisible = false;
        simulationLifeCycle.resumeGame();
    }

    public void dispose() {
        // Dispose the textures when they are no longer needed
        pauseButtonTexture.dispose();
        exitButtonTexture.dispose();
        playButtonTexture.dispose();
        questPopupTexture.dispose();
        volumeUpButtonTexture.dispose();
        volumeDownButtonTexture.dispose();
        font.dispose();
    }

    public boolean isPaused() {
        return isPaused;
    }
}
