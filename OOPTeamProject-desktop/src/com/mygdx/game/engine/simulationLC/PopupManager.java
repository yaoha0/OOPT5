package engine.simulationLC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.screens.PlayScreen;

public class PopupManager {
    private SpriteBatch batch;
    private Texture pauseButtonTexture;
    private Texture exitButtonTexture;
    private Texture playButtonTexture;
    private Texture infopopupTexture;
    private Texture questPopupTexture;
    public boolean questPopupVisible = false;
    public boolean isPopupVisible;
    public boolean infoPopupVisible = false;
    private boolean isPaused;
    private SimulationLifeCycle simulationLifeCycle;
    private Camera camera; // Add camera attribute
    private RiddleGenerator riddleGenerator;
    private BitmapFont font;

    public PopupManager(SpriteBatch batch, SimulationLifeCycle simulationLifeCycle, Camera camera,RiddleGenerator riddleGenerator) {
        this.batch = batch;
        this.simulationLifeCycle = simulationLifeCycle;
        this.camera = camera; // Assign the camera
        this.riddleGenerator = riddleGenerator;
        this.font = new BitmapFont(Gdx.files.internal("simulationLC/textfont.fnt"));
        font.getData().setScale(0.65f);
        font.setColor(1, 1, 1, 1);

        // Load pause and exit button textures
        pauseButtonTexture = new Texture("simulationLC/pause.png");
        exitButtonTexture = new Texture("simulationLC/exit.png");
        playButtonTexture = new Texture("simulationLC/playbtn.png");
        infopopupTexture = new Texture("simulationLC/infopopup.png");
        questPopupTexture = new Texture("simulationLC/questbg.png");
    }

    public void togglePopupVisibility() {
        isPopupVisible = !isPopupVisible;
    }

    public void toggleGamePause() {
        // Toggle the game's pause state and perform any necessary actions
        isPaused = !isPaused;
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

    public void showinfoPopup() {
        infoPopupVisible = true;
        simulationLifeCycle.pauseGame();
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

            if (isPaused) {
                batch.draw(playButtonTexture, buttonsX, buttonY, buttonWidth, buttonHeight);
            } else {
                batch.draw(pauseButtonTexture, buttonsX, buttonY, buttonWidth, buttonHeight);
            }

            // Draw exit button
            float exitButtonX = buttonsX + buttonWidth + buttonSpacing;
            batch.draw(exitButtonTexture, exitButtonX, buttonY, buttonWidth, buttonHeight);


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
            batch.draw(infopopupTexture, imageX, imageY, imageWidth, imageHeight);

        }

        batch.end();
    }

    public void resumeGame() {
        isPaused = false;
        // Additional logic to resume the game
        simulationLifeCycle.resumeGame();
        isPopupVisible = false;
    }

    public boolean isPopupVisible() {
        // so that my PlayScreen can access
        return isPopupVisible;
    }

    public void showQuestPopup() {
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
        font.dispose();
    }
}
