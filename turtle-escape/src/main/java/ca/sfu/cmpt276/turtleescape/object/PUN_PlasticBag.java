package ca.sfu.cmpt276.turtleescape.object;

import java.io.IOException;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

/**
 * Represents a plastic bag punishment object.
 * If the player collides, they lose score.
 */
public class PUN_PlasticBag extends SuperPunishment {

    public PUN_PlasticBag(GamePanel gp, int tileSizeX, int tileSizeY) {

        this.gp = gp;
        this.worldX = gp.tileSize * tileSizeX;
        this.worldY = gp.tileSize * tileSizeY;

        name = "PlasticBag";
        penalty = 100;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/plastic_bag.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}