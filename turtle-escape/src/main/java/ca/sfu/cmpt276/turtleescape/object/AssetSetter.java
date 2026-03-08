package ca.sfu.cmpt276.turtleescape.object;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

/**
 * Handles placing all interactive objects (rewards, items) onto the game map.
 * Keeps object placement logic separate from the main GamePanel.
 */
public class AssetSetter {

    /** Reference to the game panel for accessing the object array and tile size */
    GamePanel gp;

    /**
     * Constructs an AssetSetter with a reference to the GamePanel.
     *
     * @param gp the GamePanel this asset setter belongs to
     */
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Creates and places all objects on the map at their designated grid positions.
     * Objects are placed by multiplying column/row by tileSize to get world coordinates.
     * Adjust the positions below to match your map layout.
     */
    public void setObject() {

        // Regular rewards (seaweed) — player must collect all of these to win
        gp.obj[0] = new OBJ_Seaweed();
        gp.obj[0].worldX = 20 * gp.tileSize;
        gp.obj[0].worldY = 8 * gp.tileSize;

        gp.obj[1] = new OBJ_Seaweed();
        gp.obj[1].worldX = 27 * gp.tileSize;
        gp.obj[1].worldY = 8 * gp.tileSize;

        gp.obj[2] = new OBJ_Seaweed();
        gp.obj[2].worldX = 30 * gp.tileSize;
        gp.obj[2].worldY = 13 * gp.tileSize;

        gp.obj[3] = new OBJ_Seaweed();
        gp.obj[3].worldX = 22 * gp.tileSize;
        gp.obj[3].worldY = 16 * gp.tileSize;

        // Slots 4 and 5 reserved for randomly spawning ice cream (below kids)
    }
}
