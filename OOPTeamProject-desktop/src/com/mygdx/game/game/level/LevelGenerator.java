package game.level;

import com.badlogic.gdx.math.MathUtils;
import engine.entity.EntityManager;
import game.entity.Collectible;
import game.entity.Platform;
import game.entity.Spaceship;
import engine.entity.EntityFactory;
import engine.entity.EntityFactory.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LevelGenerator {
    private Spaceship spaceship; // Declare the spaceship variable in the class
    private ArrayList<Platform> platforms;
    private String letterBasePath;
    private int letterIndex = 0;

    public ArrayList<Float> getHolePositions() {
        return holePositions;
    }
    private List<Character> letterPool = new ArrayList<>();

    private ArrayList<Float> holePositions = new ArrayList<>();
    private EntityManager entityManager;
    private float platformWidth;
    private float groundPlatformHeight;
    private float spaceAboveGroundPlatform;
    private float levelLength;




    public LevelGenerator(float platformWidth, float groundPlatformHeight, float spaceAboveGroundPlatform, float levelLength, String letterBasePath, EntityManager entityManager) {
        this.platformWidth = platformWidth;
        this.groundPlatformHeight = groundPlatformHeight;
        this.spaceAboveGroundPlatform = spaceAboveGroundPlatform;
        this.levelLength = levelLength;
        this.letterBasePath = letterBasePath;
        this.entityManager = entityManager;
        this.platforms = new ArrayList<>();
    }

    private void initializeLetterPool(String[] allLetters, String targetWord) {
        letterPool.clear();

        // Convert the targetWord to uppercase and prepare a list of its letters
        List<Character> targetWordLetters = new ArrayList<>();
        for (char c : targetWord.toUpperCase().toCharArray()) {
            targetWordLetters.add(c);
        }

        // Define the total number of letters to spawn, ensure it's at least as long as the target word
        int totalLettersToSpawn = Math.max(20, targetWord.length() * 2); // Example to give spacing

        List<Character> randomLetters = new ArrayList<>();
        // Extract letters from allLetters that are not in the target word
        for (String letterStr : allLetters) {
            char letter = letterStr.charAt(0);
            if (!targetWordLetters.contains(letter)) {
                randomLetters.add(letter);
            }
        }
        Collections.shuffle(randomLetters);

        // Intersperse target word letters with random letters for spacing
        int targetIndex = 0, randomIndex = 0;
        while (letterPool.size() < totalLettersToSpawn) {
            // Add one target word letter and then some random letters to space out the target word
            if (targetIndex < targetWordLetters.size()) {
                letterPool.add(targetWordLetters.get(targetIndex++));
            }

            // Add 1-3 random letters between the targeted word letters for spacing
            for (int i = 0; i < MathUtils.random(1, 3) && randomIndex < randomLetters.size(); i++) {
                letterPool.add(randomLetters.get(randomIndex++));
                if (randomIndex >= randomLetters.size()) {
                    // Re-shuffle and start over with the random letters if we run out
                    Collections.shuffle(randomLetters);
                    randomIndex = 0;
                }
            }
        }

        letterIndex = 0; // Reset letter index for the new sequence
    }


    public void createFloor(String[] letters, String targetWord) {
        this.holePositions.clear();
        initializeLetterPool(letters, targetWord);

        Collections.shuffle(Arrays.asList(letters));

        int initialSafeTiles = 4;
        int finalSafeTiles = 3;
        int collectiblesSpawned = 0;
        int maxCollectibles = letters.length;
        float lastPlatformX = 0; // Variable to track the last platform's position

        float lastElevatedPlatformX = Float.MIN_VALUE; // Track the last elevated platform's X position
        float minElevatedPlatformDistance = 3 * platformWidth; // Minimum distance between elevated platforms

        // Create initial safe ground tiles
        for (float xPosition = 0; xPosition < initialSafeTiles * platformWidth; xPosition += platformWidth) {
            createPlatformAt(xPosition, 0, false, letters);
        }

        // Define the start and end of the middle section where holes and collectibles can appear
        float startOfMiddleSection = initialSafeTiles * platformWidth;
        float endOfMiddleSection = levelLength - finalSafeTiles * platformWidth;

        // Calculate the interval for placing collectibles evenly
        float sectionLength = endOfMiddleSection - startOfMiddleSection;
        float collectibleInterval = sectionLength / (maxCollectibles + 1);

        // Generating elevated platforms with increased space between them
        for (Float holeX : this.holePositions) {
            // Only create an elevated platform if we are sufficiently far from the last one
            if (holeX - lastElevatedPlatformX >= minElevatedPlatformDistance) {
                float elevationHeight = groundPlatformHeight + spaceAboveGroundPlatform;
                createPlatformAt(holeX, elevationHeight, false, letters);
                lastElevatedPlatformX = holeX; // Update the position of the last created elevated platform
            }
        }

        // Generate platforms and possibly holes in the middle section
        for (float xPosition = startOfMiddleSection, nextCollectibleX = startOfMiddleSection + collectibleInterval;
             xPosition < endOfMiddleSection;
             xPosition += platformWidth) {

            if (MathUtils.randomBoolean()) {
                this.holePositions.add(xPosition);
                continue;
            }

            boolean spawnCollectible = collectiblesSpawned < maxCollectibles && xPosition >= nextCollectibleX;
            if (spawnCollectible) {
                collectiblesSpawned++;
                nextCollectibleX += collectibleInterval;
            }

            createPlatformAt(xPosition, 0, spawnCollectible, letters);
        }

        // Generate the final safe ground tiles towards the end of the level
        for (float xPosition = endOfMiddleSection; xPosition < levelLength; xPosition += platformWidth) {
            createPlatformAt(xPosition, 0, false, letters);
            lastPlatformX = xPosition; // Update the last platform's position
        }

        // Optionally, create elevated platforms directly above the holes if needed
        for (Float holeX : this.holePositions) {
            float elevationHeight = groundPlatformHeight + spaceAboveGroundPlatform;

            // Decide randomly whether to spawn a collectible on this elevated platform
            boolean spawnCollectible = MathUtils.randomBoolean();
            createPlatformAt(holeX, elevationHeight, spawnCollectible, letters);
        }

        // After all platforms have been created, spawn the spaceship
        float spaceshipX = lastPlatformX + platformWidth / 2; // Center on the last platform

        // Assume the height of the platform image is known (e.g., 20 pixels)
        float platformImageHeight = 20; // Replace with your platform's actual height
        float spaceshipY = groundPlatformHeight + platformImageHeight; // Position on top of the platform

        createSpaceshipAt(spaceshipX, spaceshipY);
    }

    // You would need to implement this method to spawn the spaceship at the given position
    public void createSpaceshipAt(float x, float y) {
        // Create and add the spaceship entity
        Spaceship spaceship = (Spaceship) EntityFactory.createEntity(EntityType.SPACESHIP, "entity/objects/spaceship.png", x - 80, y, Spaceship.WIDTH, Spaceship.HEIGHT);
        spaceship.setPosition(x, y);
        EntityManager.getInstance().addEntity(spaceship);
    }

    // Then, modify your createPlatformAt method to include the letter character in the Collectible
    private void createPlatformAt(float x, float y, boolean spawnCollectible, String[] letters) {
        Platform platform = (Platform) EntityFactory.createEntity(EntityType.PLATFORM, "entity/objects/ground2.png", x, y, platformWidth, groundPlatformHeight);
        platforms.add(platform);
        entityManager.addEntity(platform);

        if (spawnCollectible && !letterPool.isEmpty()) {
            float collectibleY = y + groundPlatformHeight + 10; // Adjust 10 to the appropriate height above the platform
            float collectibleX = x + (platformWidth - 50) * MathUtils.random(); // Randomize collectible on the platform

            char currentLetterChar = letterPool.get(letterIndex % letterPool.size());
            String imagePath = letterBasePath + currentLetterChar + ".png";
            Collectible collectible = (Collectible) EntityFactory.createEntity(EntityType.COLLECTIBLE, imagePath, collectibleX, collectibleY, 50, 50, currentLetterChar);
            entityManager.addEntity(collectible);

            letterIndex++; // Move to the next letter
        }
    }


    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public int getLetterIndex() {
        return letterIndex;
    }
}
