package ca.sfu.cmpt276.turtleescape.object;

import java.io.IOException;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

/**
 * Represents a jellyfish regular reward object.
 * The player must collect all jellyfish to win the game.
 */
public class OBJ_Jellyfish extends SuperObject {

    GamePanel gp;
    /**
     * Constructs a jellyfish reward and loads its sprite image.
     */
    public OBJ_Jellyfish(GamePanel gp) {

        this.gp = gp;

        name = "Jellyfish";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/jellyfish.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
