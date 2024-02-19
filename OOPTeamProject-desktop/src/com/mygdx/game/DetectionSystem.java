package com.mygdx.game;

public class DetectionSystem {
	
    public boolean playerDetected(Entity ai, Entity player, int detectionRange) {
        double distanceSquared = Math.pow(ai.getX() - player.getX(), 2) + Math.pow(ai.getY() - player.getY(), 2);
        double distance = Math.sqrt(distanceSquared);
        return distance <= detectionRange;
    }
}