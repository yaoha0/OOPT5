package simulationLC;

public class SimulationLifeCycle {
    private boolean isPaused;

    private boolean isExiting;
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
