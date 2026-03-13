package ca.sfu.cmpt276.turtleescape.object;

import java.io.IOException;

import static javax.imageio.ImageIO.read;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;


public class NPC_Kid extends SuperObject {

    GamePanel gp;

    public NPC_Kid(GamePanel gp) {
        this.gp = gp;
        name = "Kid";
        collision = true;
        try {
            image = read(getClass().getClassLoader().getResourceAsStream("enemies/kid.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
