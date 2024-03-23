package engine.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import game.entity.Player;

public class CameraManager {
    private OrthographicCamera camera;
    private float viewportWidth;
    private float viewportHeight;
    private float levelLength;
    private float deadZoneRight;
    private float lerpFactor;

    private int width, height;

    public CameraManager(float viewportWidth, float viewportHeight, float levelLength, float lerpFactor, int width, int height) {
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, viewportWidth, viewportHeight);
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.levelLength = levelLength;
        this.deadZoneRight = viewportWidth * 0.25f; // 25% of the viewport width
        this.lerpFactor = lerpFactor;
        this.width = width;
        this.height = height;
    }

    public void initializeCamera(Player player) {
        float initialCameraX = Math.max(player.getX() + player.getWidth() / 2, viewportWidth / 2);
        float initialCameraY = Math.max(player.getY() + player.getHeight() / 2, viewportHeight / 2);

        camera.position.set(initialCameraX, initialCameraY, 0);
        camera.update();
    }

    public void update(Player player) {
        // Update camera to follow the player when moving past the dead zone
        float deadZoneRight = camera.viewportWidth * 0.25f; // For a dead zone on the right

        // Calculate the camera's right boundary limit
        float cameraRightLimit = levelLength - camera.viewportWidth / 2;

        // Only move camera if the player is beyond the dead zone and the camera is within the level boundary
        if (player.getX() + player.getWidth() / 2 > camera.position.x + deadZoneRight) {
            // Calculate potential new camera position based on player's current position
            float newCameraPositionX = player.getX() + player.getWidth() / 2 - deadZoneRight;
            // Clamp the camera's position to prevent it from showing space past the level end
            camera.position.x = Math.min(newCameraPositionX, cameraRightLimit);
        }

        // Make sure the camera doesn't show space off the left side of the world
        camera.position.x = Math.max(camera.position.x, camera.viewportWidth / 2);

        // Update camera's y position as before
        camera.position.y = Math.max((player.getY() + player.getHeight() / 2), (float) height / 2);

        // Update the camera
        camera.update();
        // Ensures the camera's bottom edge is never below the ground level
        camera.position.set(
                (player.getX() + player.getWidth() / 2),
                Math.max((player.getY() + player.getHeight() / 2), (float) height / 2),
                0
        );
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}