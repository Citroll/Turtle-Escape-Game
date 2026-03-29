package ca.sfu.cmpt276.turtleescape.entity;

import ca.sfu.cmpt276.turtleescape.integration.FakeGamePanel;
import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.object.SuperObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Player class.
 * Tests score changes from pickUpObject, death triggering from contactEnemy,
 * and invincibility timer behaviour.
 * Uses FakeGamePanel so no images, sounds, or Swing components are loaded.
 */
public class PlayerTest {

    GamePanel gp;
    Player player;

    @BeforeEach
    public void setUp() {
        gp = FakeGamePanel.build();
        player = gp.player;
        gp.gameState = GamePanel.GameState.PLAYING;
    }

    // ── pickUpObject ─────────────────────────────────────────────────────────

    /**
     * Picking up a Seaweed object should add 100 to the player's score
     * and remove the object from the map.
     */
    @Test
    public void testPickUpSeaweedAdds100Score() {
        SuperObject seaweed = makeObject("Seaweed");
        gp.obj[0] = seaweed;

        player.pickUpObject(0);

        assertEquals(100, player.score, "Seaweed should add 100 to score");
        assertNull(gp.obj[0], "Seaweed should be removed from the map");
    }

    /**
     * Picking up a Jellyfish object should add 100 to the player's score.
     */
    @Test
    public void testPickUpJellyfishAdds100Score() {
        gp.obj[0] = makeObject("Jellyfish");
        player.pickUpObject(0);
        assertEquals(100, player.score);
        assertNull(gp.obj[0]);
    }

    /**
     * Picking up a Shrimp object should add 100 to the player's score.
     */
    @Test
    public void testPickUpShrimpAdds100Score() {
        gp.obj[0] = makeObject("Shrimp");
        player.pickUpObject(0);
        assertEquals(100, player.score);
        assertNull(gp.obj[0]);
    }

    /**
     * Picking up an IceCream object should add 250 to the player's score.
     */
    @Test
    public void testPickUpIceCreamAdds250Score() {
        gp.obj[0] = makeObject("IceCream");
        player.pickUpObject(0);
        assertEquals(250, player.score, "IceCream should add 250 to score");
        assertNull(gp.obj[0]);
    }

    /**
     * Picking up multiple objects should accumulate score correctly.
     */
    @Test
    public void testPickUpMultipleObjectsAccumulatesScore() {
        gp.obj[0] = makeObject("Seaweed");
        gp.obj[1] = makeObject("Seaweed");
        gp.obj[2] = makeObject("IceCream");

        player.pickUpObject(0);
        player.pickUpObject(1);
        player.pickUpObject(2);

        assertEquals(450, player.score, "100 + 100 + 250 = 450");
    }

    /**
     * Calling pickUpObject with index 999 (no object) should not change score.
     */
    @Test
    public void testPickUpObjectWith999DoesNothing() {
        player.score = 0;
        player.pickUpObject(999);
        assertEquals(0, player.score, "Score should not change when index is 999");
    }

    // ── contactEnemy ─────────────────────────────────────────────────────────

    /**
     * contactEnemy with a valid index while not invincible should set DEAD state.
     */
    @Test
    public void testContactEnemyTriggersDeadState() {
        player.invincible = false;
        player.contactEnemy(0);
        assertEquals(GamePanel.GameState.DEAD, gp.gameState,
                "Game should enter DEAD state when enemy is contacted");
    }

    /**
     * contactEnemy with index 999 should NOT trigger DEAD state.
     */
    @Test
    public void testContactEnemyWith999DoesNotTriggerDead() {
        player.invincible = false;
        player.contactEnemy(999);
        assertNotEquals(GamePanel.GameState.DEAD, gp.gameState,
                "Game should NOT enter DEAD state when index is 999");
    }

    /**
     * contactEnemy while invincible should NOT trigger DEAD state.
     */
    @Test
    public void testContactEnemyWhileInvincibleDoesNotTriggerDead() {
        player.invincible = true;
        player.contactEnemy(0);
        assertNotEquals(GamePanel.GameState.DEAD, gp.gameState,
                "Invincible player should not die on enemy contact");
    }

    // ── invincibility ────────────────────────────────────────────────────────

    /**
     * Player should start with invincible = false.
     */
    @Test
    public void testPlayerStartsNotInvincible() {
        assertFalse(player.invincible, "Player should not start invincible");
    }

    /**
     * invincibleCounter should start at 0.
     */
    @Test
    public void testInvincibleCounterStartsAtZero() {
        assertEquals(0, player.invincibleCounter);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    /** Creates a minimal SuperObject stub with the given name. */
    private SuperObject makeObject(String name) {
        SuperObject obj = new SuperObject() {};
        obj.name = name;
        obj.solidArea = new Rectangle(0, 0, 48, 48);
        obj.solidAreaDefaultX = 0;
        obj.solidAreaDefaultY = 0;
        obj.collision = false;
        return obj;
    }
}
