package game.logic;

import engine.entity.EntityManager;
import game.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class RiddleGenerator {
    private Player player;

    private String[] riddles = {
            "I'm closest to the burning sun's embrace,\nMy surface scorched, a fiery place.\nMercury is my name, swift in my flight,\nIn the solar system, I shine bright. What am I?",
            "I'm blue and green, a world of wonder,\nWith swirling clouds, a cosmic thunder.\nLife flourishes upon my sphere,\nA jewel of the sky, oh so dear. What am I?",
            "Red and dusty, a desert vast,\nStorms rage on, they're unsurpassed.\nNamed for the god of war's domain,\nIn the heavens, I firmly reign. What am I?"
    };

    private String[] answers = {
            "MERCURY",
            "EARTH",
            "MARS"
    };

    private String[] facts = {
    		"Mercury is the smallest planet in our solar system and the closest to the Sun.\n Despite its proximity, it's not the hottest planet.",
            "Earth is the only planet in our solar system known to harbor life.\n It has a powerful magnetic field that protects us from the effects of solar wind.",
            "Mars is home to the tallest mountain in the solar system, Olympus Mons,\n which is nearly three times the height of Mount Everest."
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

    private void displayRiddleToPlayer(String riddle) {
        System.out.println("Riddle: " + riddle);
    }

    // Call this method to check if the player's collected word matches the answer
    public boolean checkPlayerAnswer() {
        return Objects.equals(player.getCollectedLetters(), currentTargetWord);
    }
}
