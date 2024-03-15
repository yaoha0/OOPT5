package aiControl;

import entity.Entity;

public class DecisionMaking {

    private DetectionSystem detectionSystem;
    private PathfindingSystem pathfindingSystem;
    private static final int LEFT_BOUNDARY = -20;
    private static final int RIGHT_BOUNDARY = 690;
    private static final int GRAVITY = 1;

    public DecisionMaking(DetectionSystem detectionSystem, PathfindingSystem pathfindingSystem) {
        this.detectionSystem = detectionSystem;
        this.pathfindingSystem = pathfindingSystem;
    }
    //Makes AI Attack player if within detection range, and idle if no players within range
    public void makeDecision(Entity ai, Entity player, int detectionRange, int speed) {
        if (detectionSystem.playerDetected(ai, player, detectionRange)) {
            attackTarget(ai, player, speed);
        } 
        else {
        	//10% chance to begin idle movement
        	if (Math.random() < 0.1) {
            pathfindingSystem.IdleMovement(ai, speed);
        	}
        }
    }

    //Makes the AI follow target, and ensures it does not go out of the game area
    public void attackTarget(Entity ai, Entity target,int speed) {
    	if (target.getX() < ai.getX() && ai.getX() > LEFT_BOUNDARY) {
    		ai.setX(ai.getX() - speed);
    	}
    	if (target.getX() > ai.getX() && ai.getX() < RIGHT_BOUNDARY) {
    		ai.setX(ai.getX() + speed);
    	}
        if (ai.getY() > 0){
            ai.setY(ai.getY() - GRAVITY);
        }
    }

}
