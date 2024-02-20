package simulationLC;

public class SimulationLifeCycle {
    private boolean isPaused;
    private boolean isExiting;

    public void pauseGame() {
        // Pause the game logic
        isPaused = true;
        // Additional code for pausing game graphics/animations if needed
    }

    public void resumeGame() {
        // Resume the game logic
        isPaused = false;
        // Additional code for resuming game graphics/animations if needed
    }

    public void exitGame() {
        // Exit the game logic
        isExiting = true;
        // Additional cleanup code if needed before exiting the game
        System.exit(0); // Terminate the application
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isExiting() {
        return isExiting;
    }
}
