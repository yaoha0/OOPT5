package engine.collision;

import com.badlogic.gdx.math.Rectangle;
import engine.entity.Entity;
import engine.entity.EntityManager;
import engine.scene.ScreenManager;

import com.badlogic.gdx.utils.Array;
import game.entity.Platform;
import game.entity.Player;

import java.util.ArrayList;

public class oldCollisionManager {
    private ScreenManager screenManager;
    private int collectibleCount;
    private ArrayList<Float> holePositions;
    private ArrayList<Platform> platforms;

    // Constants for better code clarity
    private static final float HITBOX_REDUCTION_FACTOR = 3.0f;
    private static final float PLATFORM_TOP_THICKNESS = 2.0f;
    private static final float HOLE_WIDTH = 50.0f;
    private static final float SIDE_COLLISION_OFFSET = -50.0f;
    private static final float ON_TOP_THRESHOLD = 5.0f;

    public oldCollisionManager(ScreenManager screenManager, ArrayList<Float> holePositions, ArrayList<Platform> platforms) {
        this.screenManager = screenManager;
        this.collectibleCount = 0;
        this.holePositions = holePositions;
        this.platforms = platforms;
    }

    public int getCollectibleCount() {
        return collectibleCount;
    }

    public void updateCollisions() {
        EntityManager entityManager = EntityManager.getInstance();
        Array<Entity> entities = entityManager.getEntities();

        Entity player = null;
        // Find the player entity
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                player = entity;
                break;
            }
        }

        if (player != null) {
            checkEntityCollisions(entities, player);
            checkPlatformCollisions(player);
        }
    }

    private void checkEntityCollisions(Array<Entity> entities, Entity player) {
        for (int i = 0; i < entities.size; i++) {
            Entity obj1 = entities.get(i);
            for (int j = i + 1; j < entities.size; j++) {
                Entity obj2 = entities.get(j);
                if (isColliding(obj1, obj2)) {
                    //handleCollision(obj1, obj2);
                }
            }
        }
    }

    // Un-comment and modify the checkPlatformCollisions method
    private void checkPlatformCollisions(Entity player) {
        Player ply = (Player) player;
        for (Platform platform : platforms) {
            if (isEntityCollidingWithPlatform(ply, platform)) {
                handlePlatformCollision(ply, platform);
                break; // Assume one platform collision is handled per frame
            }
        }
        checkPlayerFall(ply, platforms);
    }

    private boolean isEntityCollidingWithPlatform(Player player, Platform platform) {
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        Rectangle platformRect = new Rectangle(platform.getX(), platform.getY() + platform.getHeight() - PLATFORM_TOP_THICKNESS, platform.getWidth(), PLATFORM_TOP_THICKNESS);

        return playerRect.overlaps(platformRect) && player.getVelocityY() <= 0;
    }

    public boolean isColliding(Entity obj1, Entity obj2) {
        float hitbox = (obj1.getWidth() + obj2.getHeight()) / HITBOX_REDUCTION_FACTOR;
        double distanceSquared = Math.pow(obj1.getX() - obj2.getX(), 2) + Math.pow(obj1.getY() - obj2.getY(), 2);
        return Math.sqrt(distanceSquared) <= hitbox;
    }

    private void handlePlatformCollision(Player player, Platform platform) {
        player.land("platform", platform.getY() + platform.getHeight());
        player.setVelocityY(0); // Stop the downward movement
    }

    private void checkPlayerFall(Player player, ArrayList<Platform> platforms) {
        // Player's bottom rectangle
        Rectangle playerBottomRect = new Rectangle(
                player.getX(), player.getY(),
                player.getWidth(), 1 // 1 pixel in height for the bottom surface
        );

        // Check if the player is currently supported by a platform
        for (Platform platform : platforms) {
            Rectangle platformTopRect = new Rectangle(
                    platform.getX(), platform.getY() + platform.getHeight(),
                    platform.getWidth(), 1 // 1 pixel in height to represent the top surface
            );

            if (playerBottomRect.overlaps(platformTopRect)) {
                player.setIsOnPlatform(true);
                //player.land("platform", platform.getY() + platform.getHeight()); // Land the player on the platform
                // Player is not over a hole if they're on a platform

                return; // Player is on a platform, no need to check further
            }
        }
    }

    /*public void handleCollision(Entity obj1, Entity obj2) {
        if ((obj1 instanceof Player && obj2 instanceof Collectible) || (obj1 instanceof Collectible && obj2 instanceof Player)) {
            EntityManager.getInstance().removeEntity(obj1 instanceof Collectible ? obj1 : obj2); // Remove collectible from EntityManager
            collectibleCount += 1;
            //System.out.println("Player has collected " + collectibleCount + " collectibles");
        } else if (obj1 instanceof Player && obj2 instanceof Enemy || obj1 instanceof Enemy && obj2 instanceof Player) {
            screenManager.showEndScreen();
        }
    }*/

    /*private void handlePlatformCollision(Player player, Platform platform) {
        Rectangle platformTopRect = new Rectangle(platform.getX(), platform.getY() + platform.getHeight() - PLATFORM_TOP_THICKNESS, platform.getWidth(), PLATFORM_TOP_THICKNESS);
        Rectangle playerRect = new Rectangle(player.getX(), player.getY() + player.getVelocityY(), player.getWidth(), player.getHeight());

        if (player.getVelocityY() < 0 && playerRect.overlaps(platformTopRect)) {
            //player.land("platform", platform.getY() + platform.getHeight());
        }
    }



    private void checkPlayerFall(Player player, ArrayList<Platform> platforms) {
        // Player's bottom rectangle
        Rectangle playerBottomRect = new Rectangle(
                player.getX(), player.getY(),
                player.getWidth(), 1 // 1 pixel in height for the bottom surface
        );

        // Check if the player is currently supported by a platform
        for (Platform platform : platforms) {
            Rectangle platformTopRect = new Rectangle(
                    platform.getX(), platform.getY() + platform.getHeight(),
                    platform.getWidth(), 1 // 1 pixel in height to represent the top surface
            );

            if (playerBottomRect.overlaps(platformTopRect)) {
                player.setIsOnPlatform(true);
                //player.land("platform", platform.getY() + platform.getHeight()); // Land the player on the platform
                // Player is not over a hole if they're on a platform

                return; // Player is on a platform, no need to check further
            }
        }

        // First, check if the player is over a hole
        player.setIsOnGap(false);
        float playerMiddleX = player.getX() + player.getWidth() / 2;
        for (Float holeX : holePositions) {
            if (playerMiddleX > holeX && playerMiddleX < holeX + 50) {
                System.out.println("Player is over a hole"); // Debugging statement
                player.setIsOnGap(true);

            }
        }

        // Next, if the player is not over a hole, check if they are landing on the ground
        if (!player.getIsOnGap()) {
            if (player.getY() > 0) {
                // If the player's Y position is above 0, they are still in the air and potentially falling
                // Here, you could apply gravity or other falling logic to gradually decrease Y
            } else {
                // If the player's Y position is 0 or somehow below, adjust to exactly 0 and land them
                //player.setY(0); // Adjust player's position to ground level
                player.setIsOnGround(true); // Update player state to on the ground
                //player.land("ground", 0); // Handle landing on the ground
            }
        } else {
            player.fall();

        }
    }

    public boolean isEntityColliding(Entity obj1, Entity obj2) {
        Rectangle rect1 = new Rectangle(obj1.getX() + SIDE_COLLISION_OFFSET, obj1.getY(), obj1.getWidth(), obj1.getHeight());
        Rectangle rect2 = new Rectangle(obj2.getX(), obj2.getY(), obj2.getWidth(), obj2.getHeight());
        return rect1.overlaps(rect2);
    }

    private boolean isOnTopOfPlatform(Player player, Platform platform) {
        float playerBottomY = player.getY();
        float platformTopY = platform.getY() + platform.getHeight();
        return player.getVelocityY() < 0 && (playerBottomY >= platformTopY - ON_TOP_THRESHOLD && playerBottomY <= platformTopY + ON_TOP_THRESHOLD);
    }

    private void handleSideCollision(Player player, Platform platform) {
        // Get the previous position of the player
        float prevPlayerX = player.getPrevX();
        float prevPlayerY = player.getPrevY();

        // Get the player's bounding rectangle at the previous position
        Rectangle prevPlayerRect = new Rectangle(prevPlayerX, prevPlayerY, player.getWidth(), player.getHeight());
        // Get the platform's bounding rectangle
        Rectangle platformRect = new Rectangle(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());

        // Check if the player was previously above/below the platform and is now colliding from the side
        if (!prevPlayerRect.overlaps(platformRect)) {
            // The player is colliding from the side
            if (prevPlayerX + player.getWidth() <= platform.getX()) {
                // Collision on the left side of the platform
                player.setX(platform.getX() - player.getWidth());
            } else if (prevPlayerX >= platform.getX() + platform.getWidth()) {
                // Collision on the right side of the platform
                player.setX(platform.getX() + platform.getWidth());
            }

            // If you want to handle vertical side collisions (like hitting the head on the platform from below), do it here
            // Similar logic as above using Y coordinates

            // Stop horizontal movement when colliding with the side
            player.setVelocityX(0);
        }
    }*/


}


    /*if (player.getHasCollided()) {
        // halt player movement
        break;
    } else if (player.getIsOnGround()) {
        player.setHasCollided(false);

    } else if (isRightXColliding) {
        handleXPlatformCollisions(player, platform, "right");
    } else if (isLeftXColliding) {
        handleXPlatformCollisions(player, platform, "left");
    } else if (isTopYColliding) {
        // landing on the top of the platform
        handleYPlatformCollision(player, platform, "top");
    } else {
        // nothing
    }*/

    /*public void checkPlatformResponse(Player player, ArrayList<Platform> platforms) {
        for (Platform platform : platforms) {
            Rectangle playerBounds = player.getBounds();
            Rectangle platformBounds = platform.getBoundingBox();

            if (playerBounds.overlaps(platformBounds)) {
                float overlapDepthX = Math.min(playerBounds.x + playerBounds.width, platformBounds.x + platformBounds.width) - Math.max(playerBounds.x, platformBounds.x);
                float overlapDepthY = Math.min(playerBounds.y + playerBounds.height, platformBounds.y + platformBounds.height) - Math.max(playerBounds.y, platformBounds.y);

                // Always check Y collision
                boolean yCollisionDetected = false;
                if (overlapDepthY > 0) {
                    checkYPlatformCollisions(player, platforms);
                    yCollisionDetected = true;
                }

                // Check X collision only if no significant Y collision is detected
                if (!yCollisionDetected && overlapDepthX > 0) {
                    checkXPlatformCollisions(player, platforms);
                }
            }
        }
    }*/
    /*
//System.out.println("Player X: " + playerBounds.x + " Platform X: " + platformBounds.x + " Overlap: " + overlapDepthX);

    public void checkYPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        Rectangle playerBounds = player.getBounds();
        boolean onGround = false;
        Platform currentPlatform = null;

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();
            //System.out.println("Platform Top Y: " + platformBounds.y + " Platform Height: " + platformBounds.height);

            if (playerBounds.overlaps(platformBounds)) {
                //System.out.println("Player overlaps with platform");

                // Calculate overlap depths for both axes
                float overlapDepthY = (playerBounds.y + playerBounds.height) - platformBounds.y;

                // Check for Y-axis collision (on the ground)
                if (player.getVelocityY() <= 0 && overlapDepthY > 0) {
                    //System.out.println("Vertical collision detected - Player is on the ground");

                    player.setCurrentPlatform(platform);
                    onGround = true;
                    break;
                }
            }
        }
        player.setIsOnGround(onGround);

        if (!onGround) {
            player.setCurrentPlatform(null);
        }
    }

                    //.out.println("Overlap Depth X: " + overlapDepthX + ", Overlap Depth Y: " + overlapDepthY);
                //boolean movingTowardsPlatformHorizontally = (player.getVelocityX() > 0 && playerBounds.x < platformBounds.x) || (player.getVelocityX() < 0 && playerBounds.x > platformBounds.x);
                //boolean movingTowardsPlatformHorizontally = (player.getVelocityX() > 0 && playerBounds.x < platformBounds.x) || (player.getVelocityX() < 0 && playerBounds.x > platformBounds.x);
                //System.out.println(movingTowardsPlatformHorizontally);
                //&& !movingTowardsPlatformHorizontally

                // Check if the player is within the horizontal bounds of the platform
            boolean withinXBounds = playerBounds.x < (platformBounds.x + platformBounds.width) && (playerBounds.x + playerBounds.width) > platformBounds.x;

            // Check if the player's bottom Y is just above the platform's top Y
            boolean correctYPosition = (playerBounds.y + playerBounds.height) > platformBounds.y && (playerBounds.y + playerBounds.height) < (platformBounds.y + platformBounds.height + 0.1f);

            if (withinXBounds && correctYPosition) {
                // This is the ground platform
                System.out.println("Player is standing on the platform at X: " + platformBounds.x);
                break;
            }

    public boolean checkXPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        Rectangle playerBounds = player.getBounds();

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();

            if (playerBounds.overlaps(platformBounds)) {
                float overlapDepthX = Math.min(playerBounds.x + playerBounds.width, platformBounds.x + platformBounds.width)
                        - Math.max(playerBounds.x, platformBounds.x);

                //System.out.println("Player X: " + playerBounds.x + " Platform X: " + platformBounds.x + " Overlap: " + overlapDepthX);
                // Handle right collision (player moving right and colliding with the left side of the platform)
                if (player.getVelocityX() > 0 && playerBounds.x + playerBounds.width + tolerance > platformBounds.x && playerBounds.x < platformBounds.x + platformBounds.width) {
                    if (overlapDepthX > 0) {
                        return true;
                    }
                }

                // Handle left collision (player moving left and colliding with the right side of the platform)
                if (player.getVelocityX() < 0 && playerBounds.x - tolerance < platformBounds.x + platformBounds.width && playerBounds.x + playerBounds.width > platformBounds.x) {
                    if (overlapDepthX > 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Resolve vertical collision only if the player is falling onto the top of the platform
                if (player.getVelocityY() <= 0 && overlapDepthY > 0 && playerBounds.y > platformBounds.y) {
                    player.setY(platformBounds.y + 12f); // Place player on top of the platform
                    player.setVelocityY(0); // Stop downward movement
                    onPlatform = true;
                }

                // Handle bottom collision (player hitting the underside of the platform)
                if (player.getVelocityY() > 0 && overlapDepthY > 0 && playerBounds.y < platformBounds.y + platformBounds.height) {
                    player.setY(platformBounds.y - playerBounds.height); // Place player just below the platform
                    player.setVelocityY(0); // Stop upward movement
                }


    public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        Rectangle playerBounds = player.getBounds();
        boolean onGround = false;

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();

            if (playerBounds.overlaps(platformBounds)) {
                float overlapDepthX = Math.min(playerBounds.x + playerBounds.width, platformBounds.x + platformBounds.width) - Math.max(playerBounds.x, platformBounds.x);
                float overlapDepthY = Math.min(playerBounds.y + playerBounds.height, platformBounds.y + platformBounds.height) - Math.max(playerBounds.y, platformBounds.y);

                // Resolve vertical collision only if the player is falling onto the top of the platform
                if (player.getVelocityY() <= 0 && overlapDepthY > 0 && playerBounds.y > platformBounds.y) {
                    player.setY(platformBounds.y + 12f); // Place player on top of the platform
                    player.setVelocityY(0); // Stop downward movement
                    onGround = true;
                }

                // Resolve horizontal collision only if no vertical collision has been resolved
                // Resolve horizontal collision only if no vertical collision has been resolved
                if (onGround && overlapDepthX > 0) {
                    // Determine the side of the collision from the bounding boxes
                    System.out.println("playerBounds.x = " + playerBounds.x);
                    System.out.println("playerBounds.width = " + playerBounds.width);
                    System.out.println("platformBounds.x = " + platformBounds.x);
                    if (player.getVelocityX() > 0 && playerBounds.x + playerBounds.width > platformBounds.x) {
                        player.setX(platformBounds.x - playerBounds.width); // Move player to the left edge of the platform
                    } else if (player.getVelocityX() < 0 && playerBounds.x < platformBounds.x + platformBounds.width) {
                        player.setX(platformBounds.x + platformBounds.width); // Move player to the right edge of the platform
                    }
                    player.setVelocityX(0); // Stop horizontal movement
                }
            }
        }

        player.setIsOnGround(onGround);
    }*/




    /*public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        Rectangle playerBounds = player.getBounds();
        boolean onGround = false;

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();

            if (playerBounds.overlaps(platformBounds)) {
                //System.out.println("Player overlaps with platform");

                // Calculate overlap depths for both axes
                float overlapDepthX = Math.min(playerBounds.x + playerBounds.width, platformBounds.x + platformBounds.width) - Math.max(playerBounds.x, platformBounds.x);
                float overlapDepthY = (playerBounds.y + playerBounds.height) - platformBounds.y;

                //.out.println("Overlap Depth X: " + overlapDepthX + ", Overlap Depth Y: " + overlapDepthY);
                //boolean movingTowardsPlatformHorizontally = (player.getVelocityX() > 0 && playerBounds.x < platformBounds.x) || (player.getVelocityX() < 0 && playerBounds.x > platformBounds.x);
                boolean movingTowardsPlatformHorizontally = (player.getVelocityX() > 0 && playerBounds.x < platformBounds.x) || (player.getVelocityX() < 0 && playerBounds.x > platformBounds.x);
                System.out.println(movingTowardsPlatformHorizontally);
                // Check for X-axis collision (side collision) only if the player is on a platform
                if (onGround && overlapDepthX > 0 && movingTowardsPlatformHorizontally) {

                    System.out.println("Horizontal collision detected - Side collision");
                    // Check which side the collision is on and adjust player's X position accordingly
                    if (playerBounds.x < platformBounds.x) {
                        player.setX(platformBounds.x - playerBounds.width - 0.001f); // Move player left
                    } else {
                        player.setX(platformBounds.x + platformBounds.width + 0.001f); // Move player right
                    }
                    player.setVelocityX(0); // Stop horizontal movement
                    break; // Break the loop after handling the horizontal collision
                }

                // Check for Y-axis collision (on the ground)
                if (player.getVelocityY() <= 0 && overlapDepthY > 0 && !movingTowardsPlatformHorizontally) {
                    //System.out.println("Vertical collision detected - Player is on the ground");
                   // player.setY(platformBounds.y + 12f); // Adjust player's Y position to be on top of the platform
                    player.setVelocityY(0); // Stop the vertical movement
                    onGround = true;
                }


            }
        }

        player.setIsOnGround(onGround);
        //System.out.println("Player is " + (onGround ? "on the ground" : "in the air"));
    }*/










       /* public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        Rectangle playerBounds = player.getBounds();
        boolean collidedWithPlatform = false;

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();
            System.out.println("player velocity X: " + player.getVelocityX());
            if (playerBounds.overlaps(platformBounds) && player.getVelocityY() <= 0) {
                float overlapDepth = playerBounds.y + playerBounds.height - platformBounds.y;
                if (overlapDepth > 0) {
                    // Adjust player position by the overlap depth only, to prevent bouncing
                    player.setY(player.getY() + 0.0001f);
                    player.setVelocityY(0);

                    collidedWithPlatform = true;
                    break;
                    //System.out.println("Player Landed on Platform");

                }
            }
        }

        player.setIsOnGround(collidedWithPlatform);
    }*/


   /* public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        Rectangle playerBounds = player.getBounds();

        boolean collidedWithPlatformVertically = false;
        boolean collidedWithPlatformHorizontally = false;

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();

            // First, check for horizontal (X-axis) collisions
            if (!collidedWithPlatformHorizontally && playerBounds.overlaps(platformBounds)) {
                float overlapLeft = platformBounds.x + platformBounds.width - playerBounds.x;
                float overlapRight = playerBounds.x + playerBounds.width - platformBounds.x;

                // Moving right, check if player has reached the left edge of the platform
                if (player.getVelocityX() > 0 && overlapLeft > 0) {
                    player.setX(player.getX() - 20f); // Adjust position to resolve collision
                    player.setVelocityX(0);
                    collidedWithPlatformHorizontally = true;
                }
                // Moving left, check if player has reached the right edge of the platform
                else if (player.getVelocityX() < 0 && overlapRight > 0) {
                    player.setX(player.getX() + 20f); // Adjust position to resolve collision
                    player.setVelocityX(0);
                    collidedWithPlatformHorizontally = true;
                }
            }

            // Then, check for vertical (Y-axis) collisions if no horizontal collision has occurred
            if (!collidedWithPlatformHorizontally && playerBounds.overlaps(platformBounds)) {
                float verticalOverlap = Math.min(playerBounds.y + playerBounds.height - platformBounds.y, platformBounds.y + platformBounds.height - playerBounds.y);


                if (verticalOverlap > 0 && player.getVelocityY() <= 0) {
                    player.setY(player.getY() + 0.0001f); // Adjust position to resolve collision
                    player.setVelocityY(0);
                    collidedWithPlatformHorizontally = false;
                    player.setIsOnGround(true);
                } else if (verticalOverlap > 0 && player.getVelocityY() > 0) {
                    player.setY(player.getY() - 0.0001f); // Adjust position for collision from below
                    player.setVelocityY(0);
                }
            }
        }
    }*/



    /*// Can Collide LEft and RIGHT
    public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        Rectangle playerBounds = player.getBounds();
        boolean collidedWithPlatformVertically = false;
        boolean blocked = false;

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();

            // Check for horizontal collisions
            if (playerBounds.overlaps(platformBounds)) {
                float overlapXLeft = playerBounds.x + playerBounds.width - platformBounds.x;
                float overlapXRight = platformBounds.x + platformBounds.width - playerBounds.x;

                if (player.getVelocityX() > 0 && overlapXLeft > 0) { // Moving right
                    player.setX(playerBounds.width - 0.0001f);
                } else if (player.getVelocityX() < 0 && overlapXRight > 0) { // Moving left
                    player.setX(playerBounds.width + 0.0001f);
                    player.setVelocityX(0);
                }
            }
            // Check for vertical (Y-axis) collisions
            if (playerBounds.overlaps(platformBounds)) {
                float verticalOverlap = Math.min(playerBounds.y + playerBounds.height - platformBounds.y, platformBounds.y + platformBounds.height - playerBounds.y);


                if (verticalOverlap > 0 && player.getVelocityY() <= 0) {
                    player.setY(player.getY() + 0.001f); // Adjust position to resolve collision
                    player.setVelocityY(0);
                    collidedWithPlatformVertically = true;
                    player.setIsOnGround(true);
                } else if (verticalOverlap > 0 && player.getVelocityY() > 0) {
                    player.setY(player.getY() - 0.001f); // Adjust position for collision from below
                    player.setVelocityY(0);
                }
            }


        }


    }
    public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        boolean collidedWithPlatform = false;

        for (Platform platform : platforms) {
            Rectangle overlap = new Rectangle();
            if (Intersector.intersectRectangles(player.getBounds(), platform.getBoundingBox(), overlap)) {
                handleCollision(player, platform);
                collidedWithPlatform = true;
            }
        }

        if (!collidedWithPlatform) {
            player.setIsOnGround(false);
        }
    }

    private void handleCollision(Player player, Platform platform) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platformBounds = platform.getBoundingBox();

        // Calculate overlap depths
        float overlapDepthX = (playerBounds.x < platformBounds.x) ? (platformBounds.x - (playerBounds.x + playerBounds.width)) : (playerBounds.x - (platformBounds.x + platformBounds.width));
        float overlapDepthY = (playerBounds.y < platformBounds.y) ? (platformBounds.y - (playerBounds.y + playerBounds.height)) : (playerBounds.y - (platformBounds.y + platformBounds.height));

        // Determine primary collision direction
        boolean horizontalCollision = Math.abs(overlapDepthX) < Math.abs(overlapDepthY);
        boolean verticalCollision = !horizontalCollision;

        if (horizontalCollision && player.getVelocityX() != 0) {
            // Adjust the X position
            player.setX(player.getX() + overlapDepthX);
            player.setVelocityX(0); // Stop horizontal movement
        }

        if (verticalCollision && player.getVelocityY() != 0) {
            // Adjust the Y position
            player.setY(player.getY() + f);
            player.setVelocityY(0); // Stop vertical movement
            if (overlapDepthY < 0) {
                player.setIsOnGround(true); // Player is on the ground if they hit the platform from above
            }
        }
    }
*/

    /*public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        Rectangle playerBounds = player.getBounds();
        boolean collidedWithPlatform = false;
        float xOverlapThreshold = 5f; // Adjust this threshold as needed

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();
            if (playerBounds.overlaps(platformBounds)) {
                float overlapDepthXLeft = Math.max(0, platformBounds.x + platformBounds.width - playerBounds.x);
                float overlapDepthXRight = Math.max(0, playerBounds.x + playerBounds.width - platformBounds.x);

                System.out.println("overLapDepthXLeft: " + overlapDepthXLeft);
                // Side collision logic

                if (overlapDepthXLeft > 0 && player.getVelocityX() > 0 && overlapDepthXLeft >= overlapDepthXRight && overlapDepthXLeft >= xOverlapThreshold) { // Moving left
                    player.setX(platformBounds.x + platformBounds.width); // Align player to right edge of platform
                    player.setVelocityX(0); // Stop horizontal movement
                    collidedWithPlatform = true;
                } else if (overlapDepthXRight > 0 && player.getVelocityX() > 0 && overlapDepthXRight >= overlapDepthXLeft && overlapDepthXRight >= xOverlapThreshold) { // Moving right
                    player.setX(platformBounds.x - playerBounds.width); // Align player to left edge of platform
                    player.setVelocityX(0); // Stop horizontal movement
                    collidedWithPlatform = true;
                }

                // Ground collision logic, only adjust Y if no side collision was processed
                if (!collidedWithPlatform) {
                    float overlapDepthY = Math.max(0, playerBounds.y + playerBounds.height - platformBounds.y);
                    float overlapDepthYBottom = Math.max(0, platformBounds.y + platformBounds.height - playerBounds.y);

                    if (overlapDepthY > 0 && player.getVelocityY() <= 0 && overlapDepthYBottom >= xOverlapThreshold) {
                        player.setY(platformBounds.y + 12f); // Align bottom of player with top of platform
                        player.setVelocityY(0); // Stop vertical movement
                        player.setIsOnGround(true); // Player is on the ground
                        collidedWithPlatform = true;
                    }
                }
            }
        }

        // If no collision detected, player is not on ground
        if (!collidedWithPlatform) {
            player.setIsOnGround(false);
        }
    }*/




    /*public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        Rectangle playerBounds = new Rectangle(player.getBounds());
        boolean collidedVertically = false;
        boolean collidedHorizontally = false;

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBoundingBox();

            // Check for vertical collisions
            if (!collidedVertically && playerBounds.overlaps(platformBounds)) {
                float overlapY = Math.min(playerBounds.y + playerBounds.height - platformBounds.y, platformBounds.y + platformBounds.height - playerBounds.y);
                if (overlapY > 0) {
                    if (player.getVelocityY() < 0) { // Moving down
                        player.setY(platformBounds.y + 0.001f);
                        collidedVertically = true;
                    } else if (player.getVelocityY() > 0) { // Moving up
                        player.setY(player.getY() - 0.001f);
                    }
                    player.setVelocityY(0);
                    player.setIsOnGround(true);
                }
            }

            // Check for horizontal collisions
            if (!collidedHorizontally && playerBounds.overlaps(platformBounds)) {
                float overlapXLeft = playerBounds.x + playerBounds.width - platformBounds.x;
                float overlapXRight = platformBounds.x + platformBounds.width - playerBounds.x;

                if (player.getVelocityX() > 0 && overlapXLeft > 0) { // Moving right
                    player.setX(platformBounds.x - playerBounds.width - 0.001f);
                    collidedHorizontally = true;
                } else if (player.getVelocityX() < 0 && overlapXRight > 0) { // Moving left
                    player.setX(platformBounds.x + platformBounds.width + 0.001f);
                    collidedHorizontally = true;
                }
                player.setVelocityX(0);
            }
        }

        if (!collidedVertically) {
            player.setIsOnGround(false);
        }

        if (!collidedHorizontally) {
            player.setIsBlocked(false);
        }
    }*/

















/*
*
            // Check for horizontal (X-axis) collisions
            if (playerBounds.overlaps(platformBounds)) {
                float horizontalOverlap = Math.min(playerBounds.x + playerBounds.width - platformBounds.x, platformBounds.x + platformBounds.width - playerBounds.x);

            }
            *
            * // Check for horizontal (X-axis) collisions
            if (!collidedWithPlatformVertically && playerBounds.overlaps(platformBounds)) {
                float horizontalOverlap = Math.min(playerBounds.x + playerBounds.width - platformBounds.x, platformBounds.x + platformBounds.width - playerBounds.x);
                if (horizontalOverlap > 0) {
                    if (player.getVelocityX() > 0) { // Moving right
                        player.setX(player.getX() - 0.001f); // Adjust position to resolve collision
                        player.setY(player.getY());
                    } else if (player.getVelocityX() < 0) { // Moving left
                        player.setX(player.getX() + 0.001f); // Adjust position to resolve collision
                        player.setY(player.getY());
                    }
                    collidedWithPlatformHorizontally = true;
                }
            }
*
* */


//    public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
//        Rectangle playerBounds = player.getBounds();
//        boolean collidedWithPlatformVertically = false;
//        boolean collidedWithPlatformHorizontally = false;
//
//        for (Platform platform : platforms) {
//            Rectangle platformBounds = platform.getBoundingBox();
//
//            // Check for vertical (Y-axis) collisions
//            if (playerBounds.overlaps(platformBounds)) {
//                float verticalOverlap = Math.min(playerBounds.y + playerBounds.height - platformBounds.y, platformBounds.y + platformBounds.height - playerBounds.y);
//                if (verticalOverlap > 0 && player.getVelocityY() <= 0) {
//                    player.setY(player.getY() + verticalOverlap); // Adjust position to resolve collision
//                    player.setVelocityY(0);
//                    collidedWithPlatformVertically = true;
//                    player.setIsOnGround(true);
//                }
//            }
//
//            // Check for horizontal (X-axis) collisions
//            if (playerBounds.overlaps(platformBounds)) {
//                float horizontalOverlap = Math.min(playerBounds.x + playerBounds.width - platformBounds.x, platformBounds.x + platformBounds.width - playerBounds.x);
//                if (horizontalOverlap > 0) {
//                    if (player.getVelocityX() > 0) { // Moving right
//                        player.setX(player.getX() - 0.001f); // Adjust position to resolve collision
//                    } else if (player.getVelocityX() < 0) { // Moving left
//                        player.setX(player.getX() + 0.001f); // Adjust position to resolve collision
//                    }
//                    collidedWithPlatformHorizontally = true;
//                }
//            }
//        }


//
//        if (!collidedWithPlatformVertically) {
//            player.setIsOnGround(false);
//        }
//
//        // Debug output
//        System.out.println("Player X: " + player.getX() + ", Y: " + player.getY());
//        System.out.println("Collided Vertically: " + collidedWithPlatformVertically + ", Horizontally: " + collidedWithPlatformHorizontally);
//    }





/*    public void checkPlatformCollisions() {
        // Handle the collision by adjusting the player's position
        if (player.getX() < platform.getX()) {
            // If the player is to the left of the platform, move the player to the left
            player.setX(platform.getX() -  0.0001f);
        } else {
            // Otherwise, move the player to the right
            player.setX(platform.getX() + 0.0001f);
        }

        else if (minOverlap == overlapTop && player.getVelocityY() > 0) {
                    // Collision from below (player hitting the platform's bottom)
                    player.setY(player.getY() - overlapTop);
                    player.setVelocityY(0);
                    break;
                } else if (minOverlap == overlapLeft && player.getVelocityX() > 0) {
                    // Collision from the right (player moving to the left)
                    player.setX(player.getX() - overlapLeft);
                    break;
                } else if (minOverlap == overlapRight && player.getVelocityX() < 0) {
                    // Collision from the left (player moving to the right)
                    player.setX(player.getX() + overlapRight);
                    break;
                }
    }*/

//    public void checkPlatformCollisions(Player player, ArrayList<Platform> platforms) {
//        Rectangle playerBounds = player.getBounds();
//        boolean collidedWithPlatform = false;
//
//        for (Platform platform : platforms) {
//            Rectangle platformBounds = platform.getBoundingBox();
//            if (playerBounds.overlaps(platformBounds) && player.getVelocityY() <= 0) {
//                float overlapDepth = playerBounds.y + playerBounds.height - platformBounds.y;
//                if (overlapDepth > 0) {
//                    // Adjust player position by the overlap depth only, to prevent bouncing
//                    player.setY(player.getY() + 0.0001f);
//                    player.setVelocityY(0);
//
//                    collidedWithPlatform = true;
//                    break;
//                    //System.out.println("Player Landed on Platform");
//
//                }
//            }
//        }
//
//        player.setIsOnGround(collidedWithPlatform);
//    }

