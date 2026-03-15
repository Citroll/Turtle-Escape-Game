package ca.sfu.cmpt276.turtleescape.enemy;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.entity.Entity;

import java.util.Random;

public class ENEMY_seagull extends Entity {


    // Beach boundary in tile coordinates
    private static final int MIN_COL = 1;
    private static final int MAX_COL = 20;
    private static final int MIN_ROW = 1;
    private static final int MAX_ROW = 21;

    private final Random random = new Random();


    public ENEMY_seagull(GamePanel gp) {
        super(gp);

        name = "Little Kid";
        type = 1;

        speed = 2;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage(){
        up1 = setUp("enemies/seagullUp");
        up2 = setUp("enemies/seagullUp");
        down1 = setUp("enemies/seagullDown");
        down2 = setUp("enemies/seagullDown");
        left1 = setUp("enemies/seagullLeft");
        left2 = setUp("enemies/seagullLeft");
        right1 = setUp("enemies/seagullRight");
        right2 = setUp("enemies/seagullRight");
    }

    public void update() {
        super.update();

        int currentCol = worldX / gp.tileSize;
        int currentRow = worldY / gp.tileSize;

        if (currentCol < MIN_COL) {
            worldX = MIN_COL * gp.tileSize;
            direction = "right";
            // nudge vertically so they don't stack
            worldY += (random.nextInt(3) - 1) * gp.tileSize;
        }
        if (currentCol > MAX_COL) {
            worldX = MAX_COL * gp.tileSize;
            direction = "left";
            worldY += (random.nextInt(3) - 1) * gp.tileSize;
        }
        if (currentRow < MIN_ROW) {
            worldY = MIN_ROW * gp.tileSize;
            direction = "down";
            worldX += (random.nextInt(3) - 1) * gp.tileSize;
        }
        if (currentRow > MAX_ROW) {
            worldY = MAX_ROW * gp.tileSize;
            direction = "up";
            worldX += (random.nextInt(3) - 1) * gp.tileSize;
        }

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
        boolean playerOnBeach = playerCol >= MIN_COL && playerCol <= MAX_COL
                && playerRow >= MIN_ROW  && playerRow <= MAX_ROW;

        if (!onPath && tileDistance < 20 && playerOnBeach) onPath = true;
        if (onPath && (tileDistance > 30 || !playerOnBeach)) onPath = false;

        if (onPath) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
            searchPath(goalCol, goalRow);
        } else {

            actionLockCounter++;

            if (actionLockCounter == 120) {

                Random random = new Random();
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
