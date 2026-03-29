package ca.sfu.cmpt276.turtleescape.integration;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.entity.Player;
import ca.sfu.cmpt276.turtleescape.object.SuperObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the player object pickup pipeline.
 * Tests that checkObject (CollisionChecker) + pickUpObject (Player) work
 * together: objects in range are detected, score increases, and objects are
 * removed. Objects out of range are ignored.
 */
public class PlayerObjectPickupTest {

    GamePanel gp;
    Player player;

    @BeforeEach
    public void setUp() {
        gp = FakeGamePanel.build();
        player = gp.player;
        player.score = 0;
        gp.gameState = GamePanel.GameState.PLAYING;
    }

    /**
     * checkObject should return the index of an object that overlaps the player.
     */
    @Test
    public void testCheckObjectDetectsOverlappingObject() {
        SuperObject seaweed = makeObject("Seaweed");
        seaweed.worldX = player.worldX;
        seaweed.worldY = player.worldY;
        gp.obj[0] = seaweed;

        player.direction = "down";
        int index = gp.cChecker.checkObject(player, true);

        assertEquals(0, index, "checkObject should return 0 for overlapping object");
    }

    /**
     * checkObject should return 999 when no object overlaps the player.
     */
    @Test
    public void testCheckObjectReturns999WhenNoOverlap() {
        SuperObject seaweed = makeObject("Seaweed");
        seaweed.worldX = player.worldX + 10 * gp.tileSize;
        seaweed.worldY = player.worldY;
        gp.obj[0] = seaweed;

        player.direction = "down";
        int index = gp.cChecker.checkObject(player, true);

        assertEquals(999, index, "checkObject should return 999 when object is far away");
    }

    /**
     * Full pipeline: player overlaps a Seaweed → score += 100, object removed.
     */
    @Test
    public void testPickupPipelineSeaweedScoreAndRemoval() {
        SuperObject seaweed = makeObject("Seaweed");
        seaweed.worldX = player.worldX;
        seaweed.worldY = player.worldY;
        gp.obj[0] = seaweed;

        player.direction = "down";
        int index = gp.cChecker.checkObject(player, true);
        player.pickUpObject(index);

        assertEquals(100, player.score, "Score should be 100 after picking up Seaweed");
        assertNull(gp.obj[0], "Seaweed should be removed after pickup");
    }

    /**
     * Full pipeline: player overlaps an IceCream → score += 250, object removed.
     */
    @Test
    public void testPickupPipelineIceCreamScoreAndRemoval() {
        SuperObject ice = makeObject("IceCream");
        ice.worldX = player.worldX;
        ice.worldY = player.worldY;
        gp.obj[0] = ice;

        player.direction = "down";
        int index = gp.cChecker.checkObject(player, true);
        player.pickUpObject(index);

        assertEquals(250, player.score);
        assertNull(gp.obj[0]);
    }

    /**
     * Picking up multiple objects in sequence should accumulate score correctly.
     */
    @Test
    public void testSequentialPickupsAccumulateScore() {
        // Place two Seaweed at the player's location
        SuperObject s1 = makeObject("Seaweed");
        SuperObject s2 = makeObject("Shrimp");
        s1.worldX = player.worldX; s1.worldY = player.worldY;
        s2.worldX = player.worldX; s2.worldY = player.worldY;
        gp.obj[0] = s1;
        gp.obj[1] = s2;

        player.direction = "down";

        int i1 = gp.cChecker.checkObject(player, true);
        player.pickUpObject(i1);
        gp.obj[i1] = null;

        int i2 = gp.cChecker.checkObject(player, true);
        player.pickUpObject(i2);

        assertEquals(200, player.score, "Two 100-point items should total 200");
    }

    /**
     * An object that the player does not overlap should leave score unchanged.
     */
    @Test
    public void testNoPickupWhenNotOverlapping() {
        SuperObject seaweed = makeObject("Seaweed");
        seaweed.worldX = player.worldX + 500;
        seaweed.worldY = player.worldY + 500;
        gp.obj[0] = seaweed;

        player.direction = "down";
        int index = gp.cChecker.checkObject(player, true);
        player.pickUpObject(index);

        assertEquals(0, player.score, "Score should not change when not overlapping object");
        assertNotNull(gp.obj[0], "Object should not be removed when not overlapping");
    }

    /**
     * checkObject should skip null slots without throwing.
     */
    @Test
    public void testCheckObjectSkipsNullSlots() {
        // All slots null
        assertDoesNotThrow(() -> {
            int index = gp.cChecker.checkObject(player, true);
            assertEquals(999, index);
        });
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private SuperObject makeObject(String name) {
        SuperObject obj = new SuperObject() {};
        obj.name = name;
        obj.solidArea = new Rectangle(0, 0, 40, 40);
        obj.solidAreaDefaultX = 0;
        obj.solidAreaDefaultY = 0;
        obj.collision = false;
        return obj;
    }
}
