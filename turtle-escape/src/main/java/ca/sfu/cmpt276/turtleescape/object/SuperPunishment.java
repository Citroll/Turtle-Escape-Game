package ca.sfu.cmpt276.turtleescape.object;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

public abstract class SuperPunishment extends SuperObject {

    public GamePanel gp;
    
    /** Score penalty applied when the player touches this */
    public int penalty;

    /**
     * Applies the punishment effect to the player.
     * Called when the player collides with this object.
     */
    public void applyPunishment(GamePanel gp) {
        gp.player.score -= penalty;
        gp.playSE(5, 0.85f);
        gp.ui.triggerFlash();
        gp.obj[findIndex(gp)] = null; // remove from map after hit
    }

    /** Finds this object's index in the obj array */
    private int findIndex(GamePanel gp) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == this) return i;
        }
        return -1;
    }
}