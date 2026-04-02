package ca.sfu.cmpt276.turtleescape.object;

import java.io.IOException;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

/**
 * Represents a seaweed regular reward object.
 * The player must collect all seaweed to win the game.
 */
public class OBJ_Seaweed extends SuperObject {

    GamePanel gp;

    /**
     * Constructs a Seaweed reward and loads its sprite image.
     */
    public OBJ_Seaweed(GamePanel gp, int tileSizeX, int tileSizeY) {

        this.gp = gp;
        this.worldX = gp.tileSize * tileSizeX;
        this.worldY = gp.tileSize * tileSizeY;

        name = "Seaweed";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/seaweed.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
