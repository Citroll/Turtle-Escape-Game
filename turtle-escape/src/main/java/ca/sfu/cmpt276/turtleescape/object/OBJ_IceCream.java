package ca.sfu.cmpt276.turtleescape.object;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Represents an ice cream bonus reward object.
 * Collecting this grants extra points but is not required to win.
 */
public class OBJ_IceCream extends SuperObject {

    /**
     * Constructs an IceCream bonus reward and loads its sprite image.
     */
    public OBJ_IceCream() {
        name = "IceCream";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/icecream.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
