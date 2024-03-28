package engine.collision;

import engine.entity.Entity;
import engine.entity.EntityManager;
import engine.ioInput.InputOutputManager;
import game.entity.Collectible;
import game.entity.Player;
import com.badlogic.gdx.math.Rectangle;

public class PlayerCollectibleCollisionHandler implements CollisionHandler {
    private EntityManager entityManager;
    private InputOutputManager inputOutputManager;

    public PlayerCollectibleCollisionHandler(EntityManager entityManager, InputOutputManager inputOutputManager) {
        this.entityManager = entityManager;
        this.inputOutputManager = inputOutputManager;
    }

    @Override
    public boolean checkCollision(Entity entity1, Entity entity2) {
        if (!(entity1 instanceof Player) || !(entity2 instanceof Collectible)) {
            return false;
        }
        Player player = (Player) entity1;
        Collectible collectible = (Collectible) entity2;
        return player.getBounds().overlaps(collectible.getBounds());
    }

    @Override
    public void handleCollision(Entity entity1, Entity entity2) {
        Player player = (Player) entity1;
        Collectible collectible = (Collectible) entity2;
        handleCollectibleCollision(player, collectible);
    }

    private void handleCollectibleCollision(Player player, Collectible collectible) {
        char letterToCollect = collectible.getLetter();

        System.out.println("letter to collect: " + letterToCollect);
        System.out.println("player collected: " + player.getCollectedLetters());

        if (player.collectLetter(letterToCollect)) {
            // Correct letter collected
            inputOutputManager.playCollectSound();
            entityManager.removeEntity(collectible);

            if (player.hasCollectedAllLetters()) {
                // Trigger events for collecting all letters
            }
        } else {
            // Incorrect letter collected, apply damage to the player
            player.reduceHealth();
            inputOutputManager.playCollectSound(); // Consider using a sound for incorrect collection
            entityManager.removeEntity(collectible); // Decide if you want to remove the collectible on incorrect collection
        }
    }
}
