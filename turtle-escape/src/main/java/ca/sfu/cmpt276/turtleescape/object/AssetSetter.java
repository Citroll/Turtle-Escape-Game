package ca.sfu.cmpt276.turtleescape.object;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.enemy.SuperEnemy;

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
        //OBJECTIVES
        placeSeaweed(5, 2, 0);
        placeSeaweed(7, 2, 3);
        placeSeaweed(9, 2, 4);
        // slot 1 = icecream
        placeKid(11, 15, 5);
        placeKid(13, 2, 6);
//        placeJellyfish(7, 5, );
//        placeShrimp(9, 5,  );
//
//        //PUNISHMENTS
//        placePlasticBag(11, 5, );
//        placePlasticBottle(13, 5, );
//        placeHook(15, 5, );
    }

    //ENEMIES
    public void setMonster() {
        gp.enemy[0] = new SuperEnemy(gp, "seagull", 1, 20, 1, 21);
        gp.enemy[0].worldX = gp.tileSize * 14;
        gp.enemy[0].worldY = gp.tileSize * 7;

        gp.enemy[1] = new SuperEnemy(gp, "seagull", 1, 20, 1, 21);
        gp.enemy[1].worldX = gp.tileSize * 14;
        gp.enemy[1].worldY = gp.tileSize * 20;

        gp.enemy[2] = new SuperEnemy(gp, "seagull", 1, 20, 1, 21);
        gp.enemy[2].worldX = gp.tileSize * 14;
        gp.enemy[2].worldY = gp.tileSize * 14;

        gp.enemy[3] = new SuperEnemy(gp, "crab", 21, 44, 1, 21);
        gp.enemy[3].worldX = gp.tileSize * 14;
        gp.enemy[3].worldY = gp.tileSize * 14;

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

    private void placeShrimp(int col, int row, int slot) {
        gp.obj[slot] = new OBJ_Shrimp(gp);
        gp.obj[slot].worldX = col * gp.tileSize;
        gp.obj[slot].worldY = row * gp.tileSize;
    }

    private void placeJellyfish(int col, int row, int slot) {
        gp.obj[slot] = new OBJ_Jellyfish(gp);
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

    private void placeHook(int col, int row, int slot) {
        gp.obj[slot] = new PUN_Hook(gp);
        gp.obj[slot].worldX = col * gp.tileSize;
        gp.obj[slot].worldY = row * gp.tileSize;
    }
}
