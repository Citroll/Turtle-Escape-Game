package ca.sfu.cmpt276.turtleescape.entity;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Base class for all entities in the game (player, enemies, NPCs).
 * Stores common properties such as position, speed, direction, and sprite images.
 */
public class Entity {

    GamePanel gp;

    /** The x-coordinate of the entity on the screen in pixels */
    public int worldX, worldY;

    /** The movement speed of the entity in pixels per update */
    public int speed;

    /** Sprite images for each direction of movement (2 frames each for animation) */
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    /** The current direction the entity is facing ("up", "down", "left", "right") */
    public String direction = "down";

    /** Tracks how many frames have passed since the last sprite swap */
    public int spriteCounter = 0;

    /** The current sprite frame number (1 or 2) used for walking animation */
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public int solidAreaDefaultX, solidAreaDefaultY;

    public boolean collisionOn = false;

    public String name;

    public int actionLockCounter;

    public boolean invincible = false;

    public int invincibleCounter = 0;

    public int type; // 0 is a player, 1 is a monster


    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Only draw if the object is within the visible screen area
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }

            g2.drawImage(image, screenX, screenY, null);
        }
    }

    /**
     * Loads a single sprite image from resources and scales it to the tile size.
     *
     * @param imagePath the filename (without extension) of the image under resources/player/
     * @return the loaded and scaled BufferedImage, or null if loading fails
     */
    public BufferedImage setUp(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public void setAction(){};


    public void update(){
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkEntity(this, gp.enemy); // stop enemies walking into each other
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == 1 && contactPlayer == true){
            if(gp.player.invincible == false){
                gp.player.score -= 100;
                gp.ui.triggerFlash();
                gp.player.invincible = true;
            }
        }

        if(collisionOn == false){
            switch(direction){
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter++;
        if(spriteCounter > 12){
            if(spriteNum == 1){
                spriteNum = 2;
            } else if (spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    };

}
