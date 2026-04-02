package ca.sfu.cmpt276.turtleescape.object;

import java.io.IOException;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

/**
 * Represents a shrimp regular reward object.
 * The player must collect all shrimp to win the game.
 */
public class OBJ_Shrimp extends SuperObject {

    GamePanel gp;

    /**
     * Constructs a Shrimp reward and loads its sprite image.
     */
    public OBJ_Shrimp(GamePanel gp, int tileSizeX, int tileSizeY) {

        this.gp = gp;
        this.worldX = gp.tileSize * tileSizeX;
        this.worldY = gp.tileSize * tileSizeY;

        name = "Shrimp";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/shrimp.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
