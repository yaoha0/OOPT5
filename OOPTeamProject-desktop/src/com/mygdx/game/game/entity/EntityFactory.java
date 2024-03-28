package game.entity;

import engine.entity.Entity;

public class EntityFactory {

    public enum EntityType {
        PLAYER,
        ENEMY,
        COLLECTIBLE,
        PLATFORM,
        SPACESHIP
        // Add other entity types as necessary
    }

    public static Entity createEntity(EntityType type, String texturePath, float x, float y, float width, float height, Object... extraParams) {
        switch (type) {
            case PLAYER:
                if (extraParams.length >= 5) {
                    String walkTexturePath = (String) extraParams[0];
                    String jumpTexturePath = (String) extraParams[1];
                    int idleFrames = (int) extraParams[2];
                    int walkFrames = (int) extraParams[3];
                    int jumpFrames = (int) extraParams[4];
                    return new Player(texturePath, walkTexturePath, jumpTexturePath, idleFrames, walkFrames, jumpFrames, x, y, width, height);
                } else {
                    throw new IllegalArgumentException("Not enough parameters for creating a Player");
                }
            case ENEMY:
                return new Enemy(texturePath, x, y, width, height);
            case COLLECTIBLE:
            	if (extraParams.length >= 1) {
                    if (extraParams[0] instanceof Character) {
                        char letter = (Character) extraParams[0];
                        return new Collectible(texturePath, x, y, width, height, letter);
                    } else {
                        throw new IllegalArgumentException("Collectible creation requires a character type for letter");
                    }
                } else {
                    throw new IllegalArgumentException("Insufficient parameters for creating a Collectible");
                }
            case PLATFORM:
                return new Platform(texturePath, x, y, width, height);
            case SPACESHIP:
                return new Spaceship(texturePath, x, y, width, height);
            default:
                throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}