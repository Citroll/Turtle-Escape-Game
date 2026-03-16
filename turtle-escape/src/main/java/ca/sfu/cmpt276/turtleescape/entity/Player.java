package ca.sfu.cmpt276.turtleescape.entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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
     * Reference to the key handler for reading keyboard input
     */
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int score;

    /**
     * whether a movement sound (walk/swim) is currently looping
     */
    boolean moveSoundPlaying = false;

    /**
     * the sound url index of the currently playing movement sound, or -1 if
     * none
     */
    int currentMoveSoundIndex = -1;

    /**
     * Constructs a Player with references to the game panel and key handler.
     * Sets default values and loads player sprite images.
     *
     * @param gp the GamePanel this player belongs to
     * @param keyH the KeyHandler used to read keyboard input
     */
    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);

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
        worldX = gp.tileSize * 3;
        worldY = gp.tileSize * 11;
        speed = 4;
        direction = "down";
        solidArea = new Rectangle(4, 4, 40, 40);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    /**
     * Loads all 8 directional sprite frames for the player turtle from
     * resources. Two frames per direction (up, down, left, right) are loaded to
     * support walking animation.
     */
    public void getPlayerImage() {
        up1 = setUp("player/TurtleUp");
        up2 = setUp("player/TurtleUp2");
        down1 = setUp("player/TurtleDown");
        down2 = setUp("player/TurtleDown2");
        left1 = setUp("player/TurtleLeft");
        left2 = setUp("player/TurtleLeft2");
        right1 = setUp("player/TurtleRight");
        right2 = setUp("player/TurtleRight2");
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
                int enemyIndex = gp.cChecker.checkEntity(this, gp.enemy);
                contactEnemy(enemyIndex);
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
                int enemyIndex = gp.cChecker.checkEntity(this, gp.enemy);
                contactEnemy(enemyIndex);
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
                int enemyIndex = gp.cChecker.checkEntity(this, gp.enemy);
                contactEnemy(enemyIndex);
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
                int enemyIndex = gp.cChecker.checkEntity(this, gp.enemy);
                contactEnemy(enemyIndex);
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

        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        
        // add at the end of update()
        int playerCol = worldX / gp.tileSize;
        int playerRow = worldY / gp.tileSize;

        // trigger win when player reaches the water ()
        if (playerCol == 97 && playerRow == 11) {
            gp.gameState = GamePanel.GameState.WIN;
        }
    }

    /**
     * Handles contact between the player and an enemy. If the player is not
     * invincible, triggers the death screen.
     *
     * @param i the index of the contacted enemy in the enemy array, or 999 if
     * none
     */
    public void contactEnemy(int i) {
        if (i != 999) {
            if (invincible == false) {
                gp.gameState = GamePanel.GameState.DEAD;
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
                        gp.ui.triggerGreenFlash();
                        break;
                    case "IceCream":
                        score += 250;
                        if (index == 1) gp.setIceCreamCollected(1);
                        if (index == 2) gp.setIceCreamCollected(2);
                        gp.obj[index] = null;
                        gp.playSE(1);
                        gp.ui.triggerGreenFlash();
                        break;
                    case "Jellyfish":
                        score += 100;
                        gp.obj[index] = null;
                        gp.playSE(1);
                        gp.ui.triggerGreenFlash();
                        break;
                    case "Shrimp":
                        score += 100;
                        gp.obj[index] = null;
                        gp.playSE(1);
                        gp.ui.triggerGreenFlash();
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

        if (invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, screenX, screenY, null);

        // Reset Alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
