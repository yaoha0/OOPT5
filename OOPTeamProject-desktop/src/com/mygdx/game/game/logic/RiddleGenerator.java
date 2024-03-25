package game.logic;

import engine.entity.EntityManager;
import game.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class RiddleGenerator {
    private Player player;

    private String[] riddles = {
            "I orbit the sun, the very first in line,\nNo rings on my fingers, no companion to find.\nNamed for a god, swift and fleet,\nIn the solar system's race, I'm hard to beat.",
            "I'm blue and green, a world of wonder,\nWith swirling clouds, a cosmic thunder.\nLife flourishes upon my sphere,\nA jewel of the sky, oh so dear. What am I?",
            "Red and dusty, a desert vast,\nStorms rage on, they're unsurpassed.\nNamed for the god of war's domain,\nIn the heavens, I firmly reign. What am I?"
    };

    private String[] answers = {
            "MERCURY",
            "EARTH",
            "MARS"
    };

    private Random random = new Random();
    private int currentRiddleIndex = -1;
    private String currentTargetWord;

    public RiddleGenerator(Player player) {
        this.player = player;
    }

    public void startNewRiddle() {
        currentRiddleIndex = random.nextInt(riddles.length);
        currentTargetWord = answers[currentRiddleIndex];

        player.setPossibleTargetWords(new String[]{currentTargetWord});
        player.setTargetWord(currentTargetWord);

        displayRiddleToPlayer(riddles[currentRiddleIndex]);

        System.out.println(riddles[currentRiddleIndex]);
        System.out.println(answers[currentRiddleIndex]);
    }

    public String getCurrentRiddle() {
        if (currentRiddleIndex >= 0 && currentRiddleIndex < riddles.length) {
            return riddles[currentRiddleIndex];
        } else {
            return "";
        }
    }

    private void displayRiddleToPlayer(String riddle) {
        System.out.println("Riddle: " + riddle);
    }

    // Call this method to check if the player's collected word matches the answer
    public boolean checkPlayerAnswer() {
        return Objects.equals(player.getCollectedLetters(), currentTargetWord);
    }
}
