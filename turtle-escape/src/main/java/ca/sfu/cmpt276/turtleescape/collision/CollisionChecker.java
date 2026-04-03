package ca.sfu.cmpt276.turtleescape.collision;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.entity.Entity;
import ca.sfu.cmpt276.turtleescape.object.SuperObject;

public class CollisionChecker {
    // reference to the main game panel
    GamePanel gp;
    // code smell fixed
    private static final int NO_CONTACT = 999;
    
    // constructor stores the game panel reference
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;

    }

    // checks if an entity would collide with a solid tile in their movement direction
    public void checkTile(Entity entity) {
        int entityLeftX   = entity.worldX + entity.solidArea.x;
        int entityRightX  = entity.worldX + entity.solidArea.x + entity.solidArea.width - 1;
        int entityTopY    = entity.worldY + entity.solidArea.y;
        int entityBottomY = entity.worldY + entity.solidArea.y + entity.solidArea.height - 1;

        int entityLeftCol   = entityLeftX / gp.tileSize;
        int entityRightCol  = entityRightX / gp.tileSize;
        int entityTopRow    = entityTopY / gp.tileSize;
        int entityBottomRow = entityBottomY / gp.tileSize;

        switch (entity.direction) {
            case "up":
                entityTopRow = clamp((entityTopY - entity.speed) / gp.tileSize, 0, gp.maxWorldRow - 1);
                entityLeftCol = clamp(entityLeftCol, 0, gp.maxWorldCol - 1);
                entityRightCol = clamp(entityRightCol, 0, gp.maxWorldCol - 1);
                // code smell fixed
                checkBaseCollision(entityLeftCol, entityTopRow, entityRightCol, entityTopRow, entity);
                // code smell fixed
                checkOverlayCollision(entityLeftCol, entityTopRow, entityRightCol, entityTopRow, entity);
                break;

            case "down":
                entityBottomRow = clamp((entityBottomY + entity.speed) / gp.tileSize, 0, gp.maxWorldRow - 1);
                entityLeftCol = clamp(entityLeftCol, 0, gp.maxWorldCol - 1);
                entityRightCol = clamp(entityRightCol, 0, gp.maxWorldCol - 1);
                // code smell fixed
                checkBaseCollision(entityLeftCol, entityBottomRow, entityRightCol, entityBottomRow, entity);
                // code smell fixed
                checkOverlayCollision(entityLeftCol, entityBottomRow, entityRightCol, entityBottomRow, entity);
                break;

            case "left":
                entityLeftCol = clamp((entityLeftX - entity.speed) / gp.tileSize, 0, gp.maxWorldCol - 1);
                entityTopRow = clamp(entityTopRow, 0, gp.maxWorldRow - 1);
                entityBottomRow = clamp(entityBottomRow, 0, gp.maxWorldRow - 1);
                // code smell fixed
                checkBaseCollision(entityLeftCol, entityTopRow, entityLeftCol, entityBottomRow, entity);
                // code smell fixed
                checkOverlayCollision(entityLeftCol, entityTopRow, entityLeftCol, entityBottomRow, entity);
                break;

            case "right":
                entityRightCol = clamp((entityRightX + entity.speed) / gp.tileSize, 0, gp.maxWorldCol - 1);
                entityTopRow = clamp(entityTopRow, 0, gp.maxWorldRow - 1);
                entityBottomRow = clamp(entityBottomRow, 0, gp.maxWorldRow - 1);
                // code smell fixed
                checkBaseCollision(entityRightCol, entityTopRow, entityRightCol, entityBottomRow, entity);
                // code smell fixed
                checkOverlayCollision(entityRightCol, entityTopRow, entityRightCol, entityBottomRow, entity);
                break;
        }
    }

    // code smell fixed
    private void checkBaseCollision(int col1, int row1, int col2, int row2, Entity entity) {
        int tileNum1 = gp.tileM.mapTileNum[col1][row1];
        int tileNum2 = gp.tileM.mapTileNum[col2][row2];
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
        }
    }

    // code smell fixed
    private void checkOverlayCollision(int col1, int row1, int col2, int row2, Entity entity) {
        int tileNum1 = gp.tileM.mapOverlayNum[col1][row1];
        int tileNum2 = gp.tileM.mapOverlayNum[col2][row2];
        if (tileNum1 != -1 && gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum1].collision) {
            entity.collisionOn = true;
        }
        if (tileNum2 != -1 && gp.tileM.tile[tileNum2] != null && gp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
        }
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    // code smell fixed
    private void applyDirectionOffset(Entity entity) {
        switch (entity.direction) {
            case "up":    entity.solidArea.y -= entity.speed; break;
            case "down":  entity.solidArea.y += entity.speed; break;
            case "left":  entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
            default: break;
        }
    }

    // code smell fixed
    private void positionSolidArea(Entity e) {
        e.solidArea.x = e.worldX + e.solidAreaDefaultX;
        e.solidArea.y = e.worldY + e.solidAreaDefaultY;
    }

    // code smell fixed
    private void positionSolidArea(SuperObject o) {
        o.solidArea.x = o.worldX + o.solidAreaDefaultX;
        o.solidArea.y = o.worldY + o.solidAreaDefaultY;
    }

    // code smell fixed
    private void resetSolidArea(Entity e) {
        e.solidArea.x = e.solidAreaDefaultX;
        e.solidArea.y = e.solidAreaDefaultY;
    }

    // code smell fixed
    private void resetSolidArea(SuperObject o) {
        o.solidArea.x = o.solidAreaDefaultX;
        o.solidArea.y = o.solidAreaDefaultY;
    }

    /**
     * Checks if the given entity is overlapping with any object in the obj array.
     * Returns the index of the object touched, or 999 if no contact.
     *
     * @param entity the entity to check (usually the player)
     * @param isPlayer true if this entity is the player (so we can handle pickup)
     * @return the index of the contacted object, or 999 if none
     */
    public int checkObject(Entity entity, boolean isPlayer) {

        // code smell fixed
        int index = NO_CONTACT;

        // loop through every object slot in the array
        for (int i = 0; i < gp.obj.length; i++) {

            if (gp.obj[i] != null) {
                // code smell fixed
                positionSolidArea(entity);
                // code smell fixed
                positionSolidArea(gp.obj[i]);

                // code smell fixed
                applyDirectionOffset(entity);

                // check if the entity's hitbox overlaps the object's hitbox
                if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                    // if the object is solid, block the entity's movement
                    if (gp.obj[i].collision) {
                        entity.collisionOn = true;
                    }
                    // if this is the player, save the index so we can handle pickup
                    if (isPlayer) {
                        index = i;
                    }
                }

                // code smell fixed
                resetSolidArea(entity);
                // code smell fixed
                resetSolidArea(gp.obj[i]);
            }
        }

        return index;
    }


    public int checkEntity(Entity entity, Entity[] target){
        // code smell fixed - magic number
        int index = NO_CONTACT;

        // loop through every object slot in the array
        for (int i = 0; i < target.length; i++) {

            if (target[i] != null && target[i] != entity) {
                // code smell fixed
                positionSolidArea(entity);
                // code smell fixed
                positionSolidArea(target[i]);

                // code smell fixed
                applyDirectionOffset(entity);

                // check if the entity's hitbox overlaps the object's hitbox
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    entity.collisionOn = true;
                    index = i;
                }

                // code smell fixed
                resetSolidArea(entity);
                // code smell fixed
                resetSolidArea(target[i]);
            }
        }

        return index;
    }

    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        if (gp.player != null) {
            // code smell fixed
            positionSolidArea(entity);
            // code smell fixed
            positionSolidArea(gp.player);

            // code smell fixed
            applyDirectionOffset(entity);

            // check if the entity's hitbox overlaps the object's hitbox
            if (entity.solidArea.intersects(gp.player.solidArea)) {
                entity.collisionOn = true;
                contactPlayer = true;
            }

            // code smell fixed
            resetSolidArea(entity);
            // code smell fixed
            resetSolidArea(gp.player);
        }
        return contactPlayer;
    }

    public boolean checkPlayerPassive(Entity entity) {
        if (gp.player == null) {
            return false;
        }

        // code smell fixed
        positionSolidArea(entity);
        // code smell fixed
        positionSolidArea(gp.player);
        boolean contact = entity.solidArea.intersects(gp.player.solidArea);
        // code smell fixed
        resetSolidArea(entity);
        // code smell fixed
        resetSolidArea(gp.player);
        return contact;
    }
}