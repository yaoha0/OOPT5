package engine.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
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
        float playerCenterX = player.getX() + player.getWidth() / 2;
        float playerCenterY = player.getY() + player.getHeight() / 2;

        // Calculate the camera's left and right boundary limits
        float cameraLeftLimit = viewportWidth / 2;
        float cameraRightLimit = levelLength - viewportWidth / 2;

        // Calculate the new camera position to center on the player
        float newCameraPositionX = playerCenterX;

        // Clamp the camera's x position to prevent it from showing space past the level start or end
        newCameraPositionX = MathUtils.clamp(newCameraPositionX, cameraLeftLimit, cameraRightLimit);

        // Interpolate the camera's position towards the new position
        camera.position.x += (newCameraPositionX - camera.position.x) * lerpFactor;

        // Calculate the camera's bottom boundary limit (if you have one, otherwise use a suitable default)
        float cameraBottomLimit = viewportHeight / 2;
        // Interpolate the camera's y position towards the new position
        camera.position.y += (playerCenterY - camera.position.y) * lerpFactor;

        // Clamp the camera's y position if necessary to prevent showing anything below the level (if applicable)
        camera.position.y = Math.max(camera.position.y, cameraBottomLimit);

        // Update the camera
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}