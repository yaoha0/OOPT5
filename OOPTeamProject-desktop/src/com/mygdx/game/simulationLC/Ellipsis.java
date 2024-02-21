package simulationLC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Ellipsis {
    private Texture texture;
    private int x;
    private int y;
    private int width;
    private int height;

    public Ellipsis(String texturePath, int x, int y, int width, int height) {
        this.texture = new Texture(texturePath);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getters and setters for position, width, height, and texture

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Dispose method to dispose the texture when done
    public void dispose() {
        texture.dispose();
    }


}

