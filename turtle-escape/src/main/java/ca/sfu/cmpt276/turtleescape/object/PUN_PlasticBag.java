package ca.sfu.cmpt276.turtleescape.object;

import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Represents a plastic bag punishment object.
 * If the player collides, they lose score.
 */
public class PUN_PlasticBag extends SuperPunishment {

    public PUN_PlasticBag() {
        name = "PlasticBag";
        penalty = 100;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/plastic_bag.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}