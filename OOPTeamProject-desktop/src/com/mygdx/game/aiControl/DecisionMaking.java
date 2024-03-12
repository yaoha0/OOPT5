package aiControl;

import entity.Entity;

public class DecisionMaking {

    private DetectionSystem detectionSystem;
    private PathfindingSystem pathfindingSystem;

    public DecisionMaking(DetectionSystem detectionSystem, PathfindingSystem pathfindingSystem) {
        this.detectionSystem = detectionSystem;
        this.pathfindingSystem = pathfindingSystem;
    }

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

    public void attackTarget(Entity ai, Entity target,int speed) {
    	if (target.getX() < ai.getX()) {
    		ai.setX(ai.getX() - speed);
    	}
    	if (target.getX() > ai.getX()) {
    		ai.setX(ai.getX() + speed);
    	}
    }

}
