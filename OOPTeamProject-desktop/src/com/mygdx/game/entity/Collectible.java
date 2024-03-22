package entity;


public class Collectible extends Entity {
    private char letter;
    public Collectible(String texturePath, float x, float y, float width, float height, char letter) {
        super(texturePath, x, y, width, height);
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }

    @Override
    public void update(float deltaTime) {
        // Update logic for collectible (if any)
    }

}
