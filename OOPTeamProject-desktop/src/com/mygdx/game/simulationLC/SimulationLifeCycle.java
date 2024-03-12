package simulationLC;

import com.mygdx.game.GameMaster;

public class SimulationLifeCycle {
    private boolean isPaused;

    private boolean isExiting;

    private GameMaster gameMaster; // Add a reference to the GameMaster

    public SimulationLifeCycle(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
    }

    public void startGame() {
        gameMaster.setScreen(gameMaster.screenManager.getMainMenuScreen());
    }
        public void nextLevel(int collectibleCount){
        // You can now use collectibleCount inside this method
    }
    public void pauseGame() {
        // the pausing is done playercontrolmanager in the update function
        isPaused = true;
    }

    public void resumeGame() {
        // Resume the game logic
        isPaused = false;
    }

    public void exitGame() {
        // Exit the game logic
        isExiting = true;
        System.exit(0); // Terminate the application
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isExiting() {
        return isExiting;
    }
}
