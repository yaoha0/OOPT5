package aiControl;

import entity.Entity;

public class PathfindingSystem {
	
	private boolean isMoving = false;
	private int randomXpos;
	private static final int LEFT_BOUNDARY = -20;
    	private static final int RIGHT_BOUNDARY = 690;

    public PathfindingSystem() {
        // Initialize pathfinding system
    	randomXpos = randomposition();
    	
    }

    public void IdleMovement(Entity ai,int speed) {
    	if (isMoving != true) {
    		randomXpos = randomposition();
	        isMoving = true;
    	}
        if (isMoving) {
        	//Moves ai to the calculated position
        	if (randomXpos == ai.getX()) {
        		isMoving = false;
        	}
        	else {
        	moveAI(ai, speed);
        	}
        }
    }
    
    private void moveAI(Entity ai, int speed) {
    	if (randomXpos < ai.getX() && ai.getX() > LEFT_BOUNDARY) {
    		ai.setX(ai.getX() - speed);
    	}
    	if (randomXpos > ai.getX() && ai.getX() < RIGHT_BOUNDARY) {
    		ai.setX(ai.getX() + speed);
    	}
    }
    
    private int randomposition() {
    	return (int) (Math.random() * RIGHT_BOUNDARY) + 1;
    }
}
