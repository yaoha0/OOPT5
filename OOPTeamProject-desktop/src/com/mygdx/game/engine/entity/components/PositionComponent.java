package engine.entity.components;

public class PositionComponent implements Component {
    private float x, y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Getter methods
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // Setter methods
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
