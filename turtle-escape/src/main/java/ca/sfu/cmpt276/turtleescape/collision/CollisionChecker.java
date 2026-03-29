package ca.sfu.cmpt276.turtleescape.collision;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.entity.Entity;

public class CollisionChecker {
    // reference to the main game panel
    GamePanel gp;
    
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

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = clamp((entityTopY - entity.speed) / gp.tileSize, 0, gp.maxWorldRow - 1);
                entityLeftCol = clamp(entityLeftCol, 0, gp.maxWorldCol - 1);
                entityRightCol = clamp(entityRightCol, 0, gp.maxWorldCol - 1);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                tileNum1 = gp.tileM.mapOverlayNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapOverlayNum[entityRightCol][entityTopRow];
                if (tileNum1 != -1 && gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum1].collision) entity.collisionOn = true;
                if (tileNum2 != -1 && gp.tileM.tile[tileNum2] != null && gp.tileM.tile[tileNum2].collision) entity.collisionOn = true;
                break;

            case "down":
                entityBottomRow = clamp((entityBottomY + entity.speed) / gp.tileSize, 0, gp.maxWorldRow - 1);
                entityLeftCol = clamp(entityLeftCol, 0, gp.maxWorldCol - 1);
                entityRightCol = clamp(entityRightCol, 0, gp.maxWorldCol - 1);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                tileNum1 = gp.tileM.mapOverlayNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapOverlayNum[entityRightCol][entityBottomRow];
                if (tileNum1 != -1 && gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum1].collision) entity.collisionOn = true;
                if (tileNum2 != -1 && gp.tileM.tile[tileNum2] != null && gp.tileM.tile[tileNum2].collision) entity.collisionOn = true;
                break;

            case "left":
                entityLeftCol = clamp((entityLeftX - entity.speed) / gp.tileSize, 0, gp.maxWorldCol - 1);
                entityTopRow = clamp(entityTopRow, 0, gp.maxWorldRow - 1);
                entityBottomRow = clamp(entityBottomRow, 0, gp.maxWorldRow - 1);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                tileNum1 = gp.tileM.mapOverlayNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapOverlayNum[entityLeftCol][entityBottomRow];
                if (tileNum1 != -1 && gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum1].collision) entity.collisionOn = true;
                if (tileNum2 != -1 && gp.tileM.tile[tileNum2] != null && gp.tileM.tile[tileNum2].collision) entity.collisionOn = true;
                break;

            case "right":
                entityRightCol = clamp((entityRightX + entity.speed) / gp.tileSize, 0, gp.maxWorldCol - 1);
                entityTopRow = clamp(entityTopRow, 0, gp.maxWorldRow - 1);
                entityBottomRow = clamp(entityBottomRow, 0, gp.maxWorldRow - 1);
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                tileNum1 = gp.tileM.mapOverlayNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapOverlayNum[entityRightCol][entityBottomRow];
                if (tileNum1 != -1 && gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum1].collision) entity.collisionOn = true;
                if (tileNum2 != -1 && gp.tileM.tile[tileNum2] != null && gp.tileM.tile[tileNum2].collision) entity.collisionOn = true;
                break;
        }
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
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

        // 999 means no object was touched
        int index = 999;

        // loop through every object slot in the array
        for (int i = 0; i < gp.obj.length; i++) {

            if (gp.obj[i] != null) {

                // Get the entity's solid area position in world coords
                entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
                entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;

                // Get the object's solid area position in world coords
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidAreaDefaultY;

                // Predict movement based on direction
                switch (entity.direction) {
                    case "up":    entity.solidArea.y -= entity.speed; break;
                    case "down":  entity.solidArea.y += entity.speed; break;
                    case "left":  entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }

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

                // Reset solid areas back to defaults
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }

        return index;
    }


    public int checkEntity(Entity entity, Entity[] target){
        // 999 means no object was touched
        int index = 999;

        // loop through every object slot in the array
        for (int i = 0; i < target.length; i++) {

            if (target[i] != null && target[i] != entity) {

                // Get the entity's solid area position in world coords
                entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
                entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;

                // Get the object's solid area position in world coords
                target[i].solidArea.x = target[i].worldX + target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].worldY + target[i].solidAreaDefaultY;

                // Predict movement based on direction
                switch (entity.direction) {
                    case "up":    entity.solidArea.y -= entity.speed; break;
                    case "down":  entity.solidArea.y += entity.speed; break;
                    case "left":  entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }

                // check if the entity's hitbox overlaps the object's hitbox
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    entity.collisionOn = true;
                    index = i;
                }

                // Reset solid areas back to defaults
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        if (gp.player != null) {

            // Get the entity's solid area position in world coords
            entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
            entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;

            // Get the object's solid area position in world coords
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidAreaDefaultY;

            // Predict movement based on direction
            switch (entity.direction) {
                case "up":    entity.solidArea.y -= entity.speed; break;
                case "down":  entity.solidArea.y += entity.speed; break;
                case "left":  entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }

            // check if the entity's hitbox overlaps the object's hitbox
            if (entity.solidArea.intersects(gp.player.solidArea)) {
                entity.collisionOn = true;
                contactPlayer = true;
            }

            // Reset solid areas back to defaults
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        }
        return contactPlayer;
    }
}