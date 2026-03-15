package ca.sfu.cmpt276.turtleescape.object;

import java.io.IOException;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

public class PUN_Hook extends SuperPunishment{
    public PUN_Hook(GamePanel gp) {

        this.gp = gp;

        name = "Hook";
        penalty = 200;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/hook.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}