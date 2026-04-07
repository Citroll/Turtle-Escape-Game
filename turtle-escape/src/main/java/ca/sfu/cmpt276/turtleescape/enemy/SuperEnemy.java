package ca.sfu.cmpt276.turtleescape.enemy;

import java.util.Random;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.entity.Entity;

public class SuperEnemy extends Entity {

    // Beach boundary in tile coordinates
    private final int minCol;
    private final int maxCol;
    private final int minRow;
    private final int maxRow;

    private final Random random = new Random();

    public SuperEnemy(GamePanel gp, String spriteName, int minCol, int maxCol, int minRow, int maxRow, int tileSizeX,
            int tileSizeY) {
        super(gp);

        this.minCol = minCol;
        this.maxCol = maxCol;
        this.minRow = minRow;
        this.maxRow = maxRow;
        this.worldX = gp.tileSize * tileSizeX;
        this.worldY = gp.tileSize * tileSizeY;

        name = spriteName;
        speed = 3;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage(spriteName);
    }

    public void getImage(String spriteName) {
        up1 = setUp("enemies/" + spriteName + "Up");
        up2 = setUp("enemies/" + spriteName + "Up");
        down1 = setUp("enemies/" + spriteName + "Down");
        down2 = setUp("enemies/" + spriteName + "Down");
        left1 = setUp("enemies/" + spriteName + "Left");
        left2 = setUp("enemies/" + spriteName + "Left");
        right1 = setUp("enemies/" + spriteName + "Right");
        right2 = setUp("enemies/" + spriteName + "Right");
    }

    public void update() {
        super.update();
        enforceBeachBoundaries();
        avoidOtherEnemies();
    }

    /**
     * Keeps this enemy within the beach boundary area.
     * If the enemy moves outside boundaries, repositions it and changes direction.
     */
    private void enforceBeachBoundaries() {
        int currentCol = worldX / gp.tileSize;
        int currentRow = worldY / gp.tileSize;

        if (currentCol < minCol) {
            worldX = minCol * gp.tileSize;
            direction = "right";
            nudgeRandomly(true);
        }
        if (currentCol > maxCol) {
            worldX = maxCol * gp.tileSize;
            direction = "left";
            nudgeRandomly(true);
        }
        if (currentRow < minRow) {
            worldY = minRow * gp.tileSize;
            direction = "down";
            nudgeRandomly(false);
        }
        if (currentRow > maxRow) {
            worldY = maxRow * gp.tileSize;
            direction = "up";
            nudgeRandomly(false);
        }
    }

    /**
     * Applies a random nudge to prevent enemies from stacking on boundaries.
     *
     * @param isVertical true to nudge vertically, false to nudge horizontally
     */
    private void nudgeRandomly(boolean isVertical) {
        int nudgeAmount = (random.nextInt(3) - 1) * gp.tileSize;
        if (isVertical) {
            worldY += nudgeAmount;
        } else {
            worldX += nudgeAmount;
        }
    }

    /**
     * Pushes this enemy away from any nearby enemies to prevent overlap.
     */
    private void avoidOtherEnemies() {
        for (int i = 0; i < gp.enemy.length; i++) {
            if (gp.enemy[i] != null && gp.enemy[i] != this) {
                int dx = worldX - gp.enemy[i].worldX;
                int dy = worldY - gp.enemy[i].worldY;
                int distance = Math.abs(dx) + Math.abs(dy);

                if (distance < gp.tileSize) {
                    // push this bird away from the other
                    if (Math.abs(dx) > Math.abs(dy)) {
                        worldX += (dx > 0) ? 2 : -2;
                    } else {
                        worldY += (dy > 0) ? 2 : -2;
                    }
                }
            }
        }
    }

    @Override
    public void setAction() {
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        // check if player is still on the beach (sand area)
        int playerCol = gp.player.worldX / gp.tileSize;
        int playerRow = gp.player.worldY / gp.tileSize;
        boolean playerOnBeach = playerCol >= minCol && playerCol <= maxCol
                && playerRow >= minRow && playerRow <= maxRow;

        if (!onPath && tileDistance < 20 && playerOnBeach)
            onPath = true;
        if (onPath && (tileDistance > 30 || !playerOnBeach))
            onPath = false;

        if (onPath) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
            searchPath(goalCol, goalRow);
        } else {

            actionLockCounter++;

            if (actionLockCounter == 120) {

                int i = random.nextInt(100) + 1;

                if (i < 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75 && i <= 100) {
                    direction = "right";
                }

                actionLockCounter = 0;
            }
        }
    }

}
