package engine.collision;

import engine.entity.Entity;
import engine.ioInput.InputOutputManager;
import engine.scene.ScreenManager;
import game.entity.Enemy;
import game.entity.Player;

public class PlayerEnemyCollisionHandler implements CollisionHandler {
    private InputOutputManager inputOutputManager;
    private ScreenManager screenManager;

    public PlayerEnemyCollisionHandler(InputOutputManager inputOutputManager, ScreenManager screenManager) {
        this.inputOutputManager = inputOutputManager;
        this.screenManager = screenManager;
    }

    @Override
    public boolean checkCollision(Entity entity1, Entity entity2) {
        if (!(entity1 instanceof Player) || !(entity2 instanceof Enemy)) {
            return false;
        }
        Player player = (Player) entity1;
        Enemy enemy = (Enemy) entity2;
        return player.getBounds().overlaps(enemy.getBounds());
    }

    @Override
    public void handleCollision(Entity entity1, Entity entity2) {
        Player player = (Player) entity1;
        Enemy enemy = (Enemy) entity2;
        handleEnemyCollision(player, enemy);
    }

    private boolean handleEnemyCollision(Player player, Enemy enemy) {
        if (player.isInvulnerable()) {
            return false;
        }

        if (player.getBounds().overlaps(enemy.getBounds())) {
            player.reduceHealth();

            if (player.getHealth() <= 0) {
                inputOutputManager.playGameOverSound();
                inputOutputManager.stopInGameSound();
                screenManager.showEndScreen();
                return true;
            }
        }
        return false;
    }
}
