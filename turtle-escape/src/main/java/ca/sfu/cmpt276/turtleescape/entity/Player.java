package ca.sfu.cmpt276.turtleescape.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.input.KeyHandler;


/**
 * Represents the player-controlled baby sea turtle.
 * Handles player movement, sprite animation, and rendering.
 * Extends Entity to inherit common position, speed, and sprite properties.
 */
public class Player extends Entity{


    /** Reference to the game panel for accessing screen/tile dimensions */
    GamePanel gp;

    /** Reference to the key handler for reading keyboard input */
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int score;

    /**
     * Constructs a Player with references to the game panel and key handler.
     * Sets default values and loads player sprite images.
     *
     * @param gp   the GamePanel this player belongs to
     * @param keyH the KeyHandler used to read keyboard input
     */
    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        this.score = 0;

        screenX = gp.screenWidth / 2;
        screenY = gp.screenHeight / 2;

        setDefaultValues();
        getPlayerImage();
    }


    /**
     * Sets the player's default starting position, speed, and direction.
     */
    public void setDefaultValues() {
        worldX = gp.tileSize * 16;
        worldY = gp.tileSize * 10;
        speed = 4;
        direction = "down";
    }

    /**
     * Loads all directional sprite images for the player turtle from resources.
     * Each direction has two frames (1 and 2) to create a walking animation.
     */
    public void getPlayerImage(){
        try {
            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/TurtleUp.png"));
            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/TurtleUp2.png"));
            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/TurtleDown.png"));
            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/TurtleDown2.png"));
            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/TurtleLeft.png"));
            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/TurtleLeft2.png"));
            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/TurtleRight.png"));
            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/TurtleRight2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the player's position and animation frame based on keyboard input.
     * Called once per game tick. Moves the player by {@code speed} pixels in the
     * pressed direction and alternates the sprite frame every 20 ticks.
     */
    public void update(){
        // Only update player moves when keys are pressed
        if(keyH.downPressed || keyH.leftPressed || keyH.upPressed || keyH.rightPressed) {

            if(keyH.upPressed) {
                direction = "up";
                worldY -= speed; // Y values decrease as they go up, top corner is X:0, Y:0
            } if (keyH.downPressed){
                direction = "down";
                worldY += speed ; // Y values increase as they go down
            } if (keyH.leftPressed){
                direction = "left";
                worldX -= speed; // X values decrease as they go left
            } if (keyH.rightPressed) {
                direction = "right";
                worldX += speed; // X values increase as they go right
            }

            // Change the image of the sprite every 20 frames
            spriteCounter++;
            if(spriteCounter > 15) {
                if(spriteNum == 1){
                    spriteNum = 2;
                } else if (spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }


    /**
     * Draws the player sprite on the game panel at the current position.
     * Selects the correct sprite image based on direction and animation frame.
     *
     * @param g2 the Graphics2D context used for rendering
     */
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1){
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1){
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1){
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1){
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
