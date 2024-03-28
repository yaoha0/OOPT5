package engine.entity.components;

public class SizeComponent implements Component {
    private float width, height;

    public SizeComponent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    // Getter methods
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    // Setter methods
    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
