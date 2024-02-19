package com.mygdx.game;

public class AiControlManager {

    private int speed;
    private int detectionRange;
    private DecisionMaking decisionMaking;

    public AiControlManager(int speed, int detectionRange, DecisionMaking decisionMaking) {
        this.speed = speed;
        this.detectionRange = detectionRange;
        this.decisionMaking = decisionMaking;
    }

    public void updateAI(Entity ai, Entity player) {
        decisionMaking.makeDecision(ai, player, detectionRange, speed);
    }
}