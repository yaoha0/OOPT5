package engine.entity.components;

import com.badlogic.gdx.graphics.Texture;

public class TextureComponent implements Component {
    private Texture texture;

    public TextureComponent(Texture texture) {
        this.texture = texture;
    }

    // Getter method
    public Texture getTexture() {
        return texture;
    }

    // Setter method
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
