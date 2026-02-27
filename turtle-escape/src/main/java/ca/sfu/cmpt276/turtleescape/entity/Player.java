package ca.sfu.cmpt276.turtleescape.entity;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.input.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

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

    public void update(){
        // Only update player moves when keys are pressed
        if(keyH.downPressed || keyH.leftPressed || keyH.upPressed || keyH.rightPressed) {

            if(keyH.upPressed) {
                direction = "up";
                y -= speed; // Y values decrease as they go up, top corner is X:0, Y:0
            } if (keyH.downPressed){
                direction = "down";
                y += speed ; // Y values increase as they go down
            } if (keyH.leftPressed){
                direction = "left";
                x -= speed; // X values decrease as they go left
            } if (keyH.rightPressed) {
                direction = "right";
                x += speed; // X values increase as they go right
            }

            // Change the image of the sprite every 20 frames
            spriteCounter++;
            if(spriteCounter > 20) {
                if(spriteNum == 1){
                    spriteNum = 2;
                } else if (spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

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

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
