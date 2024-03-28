package engine.aiControl;

import game.aiControl.NonControlled;
import engine.entity.Entity;
import game.entity.Enemy;

public class AiControlManager {

    private int speed;
    private int detectionRange;
    private DecisionMaking decisionMaking;
    private NonControlled noncontrolled;
    private static final int GRAVITY = 3;

    public AiControlManager(int speed, int detectionRange, DecisionMaking decisionMaking, NonControlled noncontrolled) {
        this.speed = speed;
        this.detectionRange = detectionRange;
        this.decisionMaking = decisionMaking;
        this.noncontrolled = noncontrolled;
    }

    public void updateAI(Enemy ai, Entity player) {
        noncontrolled.FallingObject(ai, GRAVITY);
        //	decisionMaking.makeDecision(ai, player, detectionRange, speed);}
    }
}
