package ca.sfu.cmpt276.turtleescape.object;

import java.io.IOException;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

/**
 * Represents an ice cream bonus reward object.
 * Collecting this grants extra points but is not required to win.
 */
public class OBJ_IceCream extends SuperObject {

    GamePanel gp;
    /**
     * Constructs an IceCream bonus reward and loads its sprite image.
     */
    public OBJ_IceCream(GamePanel gp) {

        this.gp = gp;

        name = "IceCream";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/icecream.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
