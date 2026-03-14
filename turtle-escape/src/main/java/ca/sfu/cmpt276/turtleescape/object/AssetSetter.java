package ca.sfu.cmpt276.turtleescape.object;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.enemy.ENEMY_seagull;

/**
 * Handles placing all interactive objects (rewards, items) onto the game map.
 * Keeps object placement logic separate from the main GamePanel.
 */
public class AssetSetter {

    /**
     * Reference to the game panel for accessing the object array and tile size
     */
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
     * Creates and places all objects on the map at their designated grid
     * positions. Objects are placed by multiplying column/row by tileSize to
     * get world coordinates. Adjust the positions below to match your map
     * layout.
     */
    public void setObject() {
        placeSeaweed(25, 8, 0);
        placeSeaweed(19, 7, 1);
        // slots 2 and 3 left empty for ice cream

        placePlasticBag(21, 11, 4);
        placePlasticBag(24, 15, 5);
        placePlasticBottle(27, 11, 6);
        placePlasticBottle(27, 7, 7);

        placeKid(28, 14, 8);
        placeKid(24, 5, 9);

        placeSeaweed(15, 20, 10); // moved here
    }

    //ENEMIES
    public void setMonster() {
        gp.enemy[0] = new ENEMY_seagull(gp);
        gp.enemy[0].worldX = gp.tileSize * 26;
        gp.enemy[0].worldY = gp.tileSize * 9;

        gp.enemy[1] = new ENEMY_seagull(gp);
        gp.enemy[1].worldX = gp.tileSize * 20;
        gp.enemy[1].worldY = gp.tileSize * 10;
    }

    //NPCS
    private void placeKid(int col, int row, int slot) {
        gp.obj[slot] = new NPC_Kid(gp);
        gp.obj[slot].worldX = col * gp.tileSize;
        gp.obj[slot].worldY = row * gp.tileSize;
    }

    //REWARDS
    private void placeSeaweed(int col, int row, int slot) {
        gp.obj[slot] = new OBJ_Seaweed(gp);
        gp.obj[slot].worldX = col * gp.tileSize;
        gp.obj[slot].worldY = row * gp.tileSize;
    }

    //PUNISHMENTS
    private void placePlasticBag(int col, int row, int slot) {
        gp.obj[slot] = new PUN_PlasticBag(gp);
        gp.obj[slot].worldX = col * gp.tileSize;
        gp.obj[slot].worldY = row * gp.tileSize;
    }

    private void placePlasticBottle(int col, int row, int slot) {
        gp.obj[slot] = new PUN_PlasticBottle(gp);
        gp.obj[slot].worldX = col * gp.tileSize;
        gp.obj[slot].worldY = row * gp.tileSize;
    }
}
