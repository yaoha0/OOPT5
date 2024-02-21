package aiControl;

import entity.Entity;

public class PathfindingSystem {
	
	private boolean isMoving = false;
	private int randomXpos;
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
        	if (randomXpos < ai.getX() - 5 || randomXpos == ai.getX() || randomXpos > ai.getX() + 5) {
        		isMoving = false;
        	}
        	else {
        	moveAI(ai, speed);
        	}
        }
    }
    
    private void moveAI(Entity ai, int speed) {
    	if (randomXpos < ai.getX()) {
    		ai.setX(ai.getX() - speed);
    	}
    	if (randomXpos > ai.getX()) {
    		ai.setX(ai.getX() + speed);
    	}
    }
    
    private int randomposition() {
    	return (int) (Math.random() * 600) + 1;
    }
}
