package game.aiControl;

import com.badlogic.gdx.Gdx;
import engine.aiControl.PathfindingSystem;
import engine.entity.Entity;
import game.entity.Enemy;

public class NonControlled {

    private PathfindingSystem pathfindingSystem;
    private static final int LEFT_BOUNDARY = 0;
    private static final float RIGHT_BOUNDARY = Gdx.graphics.getWidth() * 2.8f;
    private static final int TOP_BOUNDARY = Gdx.graphics.getHeight();
    private static final int BOTTOM_BOUNDARY = 0;
    private static final int GRAVITY = 4;
    private int drift_speed = 1;
    private boolean isPaused = false;
    public NonControlled(PathfindingSystem pathfindingsystem) {
        this.pathfindingSystem = pathfindingsystem;
    }
    //Makes AI Attack player if within detection range, and idle if no players within range
    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }
    public void FallingObject(Enemy ai, int gravity) {
        if(isPaused){
            return;
        }
        //makes object fall
        if (ai.getY() > 0) {
            ai.setY(ai.getY() - gravity);
            ai.setX(ai.getX() + ai.getDriftSpeed());
        }
        //Teleport object back to the top if it goes out of bounds
        if (ai.getY() <= BOTTOM_BOUNDARY) {
            ai.setY(TOP_BOUNDARY);
            ai.setX(pathfindingSystem.randomposition());
            ai.setDriftSpeed(randomdrift());
        }
        else if (ai.getX() >= RIGHT_BOUNDARY) {
            ai.setY(TOP_BOUNDARY);
            ai.setX(pathfindingSystem.randomposition());
            ai.setDriftSpeed(randomdrift());
        }
        else if (ai.getX() <= LEFT_BOUNDARY) {
            ai.setY(TOP_BOUNDARY);
            ai.setX(pathfindingSystem.randomposition());
            ai.setDriftSpeed(randomdrift());
        }
    }
    //sets the falling object's drifting speed
    private int randomdrift() {
        float x = (int) (Math.random());
        if (x < 0.5){
            x = -1;
        }
        else {
            x = 1;
        }
        return (int) (Math.random() * 3 * x);
    }



}