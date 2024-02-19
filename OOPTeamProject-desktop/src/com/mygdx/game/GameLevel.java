package com.mygdx.game;

public class GameLevel {
    private int levelNumber;
    private String levelLayout;

    // Constructor
    public GameLevel(int levelNumber, String levelLayout) {
        this.levelNumber = levelNumber;
        this.levelLayout = levelLayout;
    }

    // Getters and setters for attributes
    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getLevelLayout() {
        return levelLayout;
    }

    public void setLevelLayout(String levelLayout) {
        this.levelLayout = levelLayout;
    }

    // Method to update the game level
    public void updateLevel(String newLayout) {
        this.levelLayout = newLayout;
        // Additional logic to update the level (if needed)
    }
}
