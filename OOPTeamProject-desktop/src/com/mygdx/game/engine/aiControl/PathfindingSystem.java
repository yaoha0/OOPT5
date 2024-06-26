package engine.aiControl;

import com.badlogic.gdx.Gdx;
import engine.entity.Entity;

public class PathfindingSystem {

	private boolean isMoving = false;
	private int randomXpos;
	private static final int LEFT_BOUNDARY = 0;
	private static final float RIGHT_BOUNDARY = Gdx.graphics.getWidth() * 2.8f;

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

	public int randomposition() {
		return (int) (Math.random() * RIGHT_BOUNDARY) + 1;
	}
}
