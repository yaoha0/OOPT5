package engine.collision;

import engine.entity.Entity;
import engine.entity.EntityManager;
import engine.ioInput.InputOutputManager;
import engine.scene.ScreenManager;
import engine.simulationLC.PopupManager;
import game.entity.Player;
import game.entity.Spaceship;
import com.badlogic.gdx.math.Rectangle;

public class PlayerSpaceshipCollisionHandler implements CollisionHandler {
    private EntityManager entityManager;
    private InputOutputManager inputOutputManager;
    private ScreenManager screenManager;
    private PopupManager popupManager;

    public PlayerSpaceshipCollisionHandler(EntityManager entityManager, InputOutputManager inputOutputManager, ScreenManager screenManager, PopupManager popupManager) {
        this.entityManager = entityManager;
        this.inputOutputManager = inputOutputManager;
        this.screenManager = screenManager;
        this.popupManager = popupManager;
    }

    @Override
    public boolean checkCollision(Entity entity1, Entity entity2) {
        if (!(entity1 instanceof Player) || !(entity2 instanceof Spaceship)) {
            return false;
        }
        Player player = (Player) entity1;
        Spaceship spaceship = (Spaceship) entity2;
        return player.getBounds().overlaps(spaceship.getBounds());
    }

    @Override
    public void handleCollision(Entity entity1, Entity entity2) {
        Player player = (Player) entity1;
        Spaceship spaceship = (Spaceship) entity2;
        handleSpaceshipCollision(player, spaceship);
    }

    private void handleSpaceshipCollision(Player player, Spaceship spaceship) {
        if (player.getBounds().overlaps(spaceship.getBounds())) {
            if (player.hasCollectedAllLetters()) {
                // Remove the spaceship from the screen and the entity manager
                entityManager.removeEntity(spaceship);

                // Trigger game over or level completion
                inputOutputManager.playWinSound();
                inputOutputManager.stopInGameSound();
                screenManager.showWinScreen();
            } else {
                // The player has not collected all letters, so they cannot end the game yet
                // Handle this case, maybe show a message or prevent certain actions
                popupManager.infoPopupVisible = true; // You need to manage how you show popups or messages
            }
        }
    }
}
