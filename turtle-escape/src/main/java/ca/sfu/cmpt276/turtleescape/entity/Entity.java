package ca.sfu.cmpt276.turtleescape.entity;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
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
    public String direction;

    /** Tracks how many frames have passed since the last sprite swap */
    public int spriteCounter = 0;

    /** The current sprite frame number (1 or 2) used for walking animation */
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public int solidAreaDefaultX, solidAreaDefaultY;

    public boolean collisionOn = false;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Loads a single player sprite image from resources and scales it to the tile size.
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
    public void update(){};

}
