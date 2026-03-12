package ca.sfu.cmpt276.turtleescape.object;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

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
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/icecream.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
