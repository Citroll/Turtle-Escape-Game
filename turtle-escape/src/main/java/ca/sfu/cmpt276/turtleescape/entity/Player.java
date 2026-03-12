package ca.sfu.cmpt276.turtleescape.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.input.KeyHandler;
import ca.sfu.cmpt276.turtleescape.object.SuperObject;
import ca.sfu.cmpt276.turtleescape.object.SuperPunishment;

/**
 * Represents the player-controlled baby sea turtle. Handles player movement,
 * sprite animation, and rendering. Extends Entity to inherit common position,
 * speed, and sprite properties.
 */
public class Player extends Entity {

    /**
     * Reference to the game panel for accessing screen/tile dimensions
     */
    GamePanel gp;

    /**
     * Reference to the key handler for reading keyboard input
     */
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int score;

    /** whether a movement sound (walk/swim) is currently looping */
    boolean moveSoundPlaying = false;

    /** the sound url index of the currently playing movement sound, or -1 if none */
    int currentMoveSoundIndex = -1;

    /**
     * Constructs a Player with references to the game panel and key handler.
     * Sets default values and loads player sprite images.
     *
     * @param gp the GamePanel this player belongs to
     * @param keyH the KeyHandler used to read keyboard input
     */
    public Player(GamePanel gp, KeyHandler keyH) {
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
        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    /**
     * Loads all directional sprite images for the player turtle from resources.
     * Each direction has two frames (1 and 2) to create a walking animation.
     */
    public void getPlayerImage() {
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
     * Updates the player's position and animation frame based on keyboard
     * input. Called once per game tick. Moves the player by {@code speed}
     * pixels in the pressed direction and alternates the sprite frame every 20
     * ticks.
     */
    public void update() {
        int moveSpeed = speed;

        // Handles diagonal movement to have the same speed as directional
        if ((keyH.upPressed || keyH.downPressed) && (keyH.leftPressed || keyH.rightPressed)) {
            moveSpeed = 3;
        }
        // Only update player moves when keys are pressed
        if (keyH.downPressed || keyH.leftPressed || keyH.upPressed || keyH.rightPressed) {
            // Handle vertical movement (up/down)
            if (keyH.upPressed) {
                direction = "up";
                collisionOn = false;
                gp.cChecker.checkTile(this);
                int objIndex = gp.cChecker.checkObject(this, true);
                pickUpObject(objIndex);
                if (!collisionOn) {
                    worldY -= moveSpeed;
                }
            }
            if (keyH.downPressed) {
                direction = "down";
                collisionOn = false;
                gp.cChecker.checkTile(this);
                int objIndex = gp.cChecker.checkObject(this, true);
                pickUpObject(objIndex);
                if (!collisionOn) {
                    worldY += moveSpeed;
                }
            }

            // Handle horizontal movement (left/right)
            if (keyH.leftPressed) {
                direction = "left";
                collisionOn = false;
                gp.cChecker.checkTile(this);
                int objIndex = gp.cChecker.checkObject(this, true);
                pickUpObject(objIndex);
                if (!collisionOn) {
                    worldX -= moveSpeed;
                }
            }
            if (keyH.rightPressed) {
                direction = "right";
                collisionOn = false;
                gp.cChecker.checkTile(this);
                int objIndex = gp.cChecker.checkObject(this, true);
                pickUpObject(objIndex);
                if (!collisionOn) {
                    worldX += moveSpeed;
                }
            }

            // Change the image of the sprite every 15 frames
            spriteCounter++;
            if (spriteCounter > 15) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

            // calculate current tile and pick movement sound to play 
            int col = (worldX + gp.tileSize / 2) / gp.tileSize;
            int row = (worldY + gp.tileSize / 2) / gp.tileSize;
            int tileNum = gp.tileM.mapTileNum[col][row];
            int soundIndex = (tileNum == 0) ? 2 : 3; // water=3, sand=2

            // Start or switch the looping move sound
            if (!moveSoundPlaying || currentMoveSoundIndex != soundIndex) {
                gp.stopMoveSE();
                gp.playMoveSE(soundIndex);
                moveSoundPlaying = true;
                currentMoveSoundIndex = soundIndex;
            }

        } else {
            // No keys pressed stop move sound
            if (moveSoundPlaying) {
                gp.stopMoveSE();
                moveSoundPlaying = false;
                currentMoveSoundIndex = -1;
            }
        }
    }

    /**
     * Handles picking up an object when the player touches it. Removes the
     * object from the map and updates the score.
     *
     * @param index the index of the object in the obj array, or 999 if none
     */
    public void pickUpObject(int index) {
        if (index != 999) {
            SuperObject obj = gp.obj[index];

            if (obj instanceof SuperPunishment) {
                ((SuperPunishment) obj).applyPunishment(gp);
            } else {
                switch (obj.name) {
                    case "Seaweed":
                        score += 100;
                        gp.obj[index] = null;
                        gp.playSE(1);
                        break;
                    case "IceCream":
                        score += 250;
                        if (index == 2) {
                            gp.setIceCreamCollected(2);
                        }
                        if (index == 3) {
                            gp.setIceCreamCollected(3);
                        }
                        gp.obj[index] = null;
                        gp.playSE(1);
                        break;
                }
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

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
