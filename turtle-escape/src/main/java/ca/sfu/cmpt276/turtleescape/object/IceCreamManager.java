package ca.sfu.cmpt276.turtleescape.object;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

/**
 * Handles random spawning and timed despawning of ice cream below kid tiles.
 * Ice cream appears one tile below each kid, stays for a few seconds, then
 * vanishes.
 */
public class IceCreamManager {

    GamePanel gp;

    // --- Ice cream spawn/despawn config ---
    /**
     * Frames between spawn attempts (~5 seconds at 60fps)
     */
    private static final int ICE_CREAM_SPAWN_INTERVAL = 300;
    /**
     * How many frames ice cream stays before vanishing (~3 seconds)
     */
    private static final int ICE_CREAM_LIFETIME = 240;
    /**
     * Frame counter for spawn timing
     */
    private int spawnTimer = 0;

    /**
     * Remaining lifetime for ice cream in slot 2
     */
    private int iceCreamLife2 = 0;

    /**
     * True once slot 2 ice cream has been collected — will not respawn
     */
    private boolean iceCream2Collected = false;

    /**
     * Constructs the GamePanel and initializes display settings. Sets preferred
     * size, background color, enables double buffering, and registers the key
     * listener for input.
     */
    public IceCreamManager(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Places a new ice cream object at the given grid position and object slot.
     *
     * @param col the tile column to place the ice cream
     * @param row the tile row to place the ice cream
     * @param slot the index in the obj array to place the ice cream
     */
    private void placeIceCream(int col, int row, int slot) {
        gp.obj[slot] = new OBJ_IceCream(gp);
        gp.obj[slot].worldX = col * gp.tileSize;
        gp.obj[slot].worldY = row * gp.tileSize;
    }

    public void update() {
        //Icecream killer
        if (gp.obj[1] != null) {
            iceCreamLife2--;
            if (iceCreamLife2 <= 0) {
                gp.obj[1] = null;
            }
        }

        //Icecream spawner
        spawnTimer++;
        if (spawnTimer >= ICE_CREAM_SPAWN_INTERVAL) {
            spawnTimer = 0;

            if (gp.obj[2] == null && !iceCream2Collected && Math.random() < 0.5) {
                placeIceCream(5, 6, 1);
                iceCreamLife2 = ICE_CREAM_LIFETIME;
            }
        }
    }

    /**
     * Marks an ice cream slot as permanently collected so it will not respawn.
     *
     * @param slot the obj array index
     */
    public void setCollected(int slot) {
        if (slot == 1) {
            iceCream2Collected = true;
        }
    }

}
