package ca.sfu.cmpt276.turtleescape.object;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Represents a seaweed regular reward object.
 * The player must collect all seaweed to win the game.
 */
public class OBJ_Seaweed extends SuperObject {

    /**
     * Constructs a Seaweed reward and loads its sprite image.
     */
    public OBJ_Seaweed() {
        name = "Seaweed";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/seaweed.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
