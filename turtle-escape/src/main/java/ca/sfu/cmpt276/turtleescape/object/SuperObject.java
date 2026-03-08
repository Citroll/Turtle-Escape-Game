package ca.sfu.cmpt276.turtleescape.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

/**
 * Base class for all interactive objects placed on the game map.
 * Holds the object's image, name, world position, collision area,
 * and provides a draw method for rendering relative to the player's camera.
 */
public class SuperObject {

    /** The image used to render object */
    public BufferedImage image;

    /** The name/type identifier of object */
    public String name;

    /** Whether this object blocks movement */
    public boolean collision = false;

    /** The object's x-coordinate in the world */
    public int worldX;

    /** The object's y-coordinate in the world */
    public int worldY;

    /** The collision/interaction area for this object */
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    /** Default x offset of the solid area */
    public int solidAreaDefaultX = 0;

    /** Default y offset of the solid area */
    public int solidAreaDefaultY = 0;

    /**
     * Draws this object on the screen, offset by the player's camera position.
     * Only draws if the object is within the visible screen area.
     *
     * @param g2 the Graphics2D context used for rendering
     * @param gp the GamePanel for screen and player position info
     */
    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Only draw if the object is within the visible screen area
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
