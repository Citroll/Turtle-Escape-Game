package ca.sfu.cmpt276.turtleescape.collision;

import ca.sfu.cmpt276.turtleescape.integration.FakeGamePanel;
import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CollisionChecker.
 * Tests checkPlayer (enemy-to-player contact detection) and checkEntity
 * (entity-to-entity contact) in isolation using FakeGamePanel.
 */
public class CollisionCheckerTest {

    GamePanel gp;
    CollisionChecker checker;

    @BeforeEach
    public void setUp() {
        gp = FakeGamePanel.build();
        checker = gp.cChecker;
    }

    // ── checkPlayer ──────────────────────────────────────────────────────────

    /**
     * An enemy entity placed exactly on top of the player should be detected
     * as contacting the player.
     */
    @Test
    public void testCheckPlayerDetectsOverlap() {
        Entity enemy = makeEntity(gp);

        // Place enemy at exactly the same world position as the player
        enemy.worldX = gp.player.worldX;
        enemy.worldY = gp.player.worldY;
        enemy.direction = "down";

        boolean contact = checker.checkPlayer(enemy);
        assertTrue(contact, "Enemy on same tile as player should return true");
    }

    /**
     * An enemy placed far away from the player should NOT be detected as contacting.
     */
    @Test
    public void testCheckPlayerNoContactWhenFarAway() {
        Entity enemy = makeEntity(gp);

        // Place enemy 10 tiles away from player
        enemy.worldX = gp.player.worldX + 10 * gp.tileSize;
        enemy.worldY = gp.player.worldY;
        enemy.direction = "down";

        boolean contact = checker.checkPlayer(enemy);
        assertFalse(contact, "Enemy far away should not contact player");
    }

    /**
     * An enemy one pixel outside the player's hitbox should NOT trigger contact.
     */
    @Test
    public void testCheckPlayerNoContactWhenJustOutside() {
        Entity enemy = makeEntity(gp);

        // Place enemy just beyond the player's solid area width
        enemy.worldX = gp.player.worldX + gp.player.solidArea.width + 1;
        enemy.worldY = gp.player.worldY;
        enemy.direction = "right";

        boolean contact = checker.checkPlayer(enemy);
        assertFalse(contact, "Enemy just outside hitbox should not contact player");
    }

    /**
     * checkPlayer should return false when gp.player is null.
     * (Defensive coding check — shouldn't crash.)
     */
    @Test
    public void testCheckPlayerWithNullPlayerReturnsFalse() throws Exception {
        FakeGamePanel.set(gp, GamePanel.class, "player", null);

        Entity enemy = makeEntity(gp);
        enemy.direction = "down";

        // Should not throw, should return false
        assertDoesNotThrow(() -> {
            boolean result = checker.checkPlayer(enemy);
            assertFalse(result);
        });
    }

    // ── checkEntity ──────────────────────────────────────────────────────────

    /**
     * checkEntity should detect when two entities overlap.
     */
    @Test
    public void testCheckEntityDetectsOverlap() {
        Entity a = makeEntity(gp);
        Entity b = makeEntity(gp);

        a.worldX = 200;
        a.worldY = 200;
        a.direction = "down";

        b.worldX = 200;
        b.worldY = 200;

        Entity[] targets = { b };
        int result = checker.checkEntity(a, targets);

        assertEquals(0, result, "Overlapping entities should return index 0");
    }

    /**
     * checkEntity should return 999 when entities are not overlapping.
     */
    @Test
    public void testCheckEntityReturns999WhenNoOverlap() {
        Entity a = makeEntity(gp);
        Entity b = makeEntity(gp);

        a.worldX = 0;
        a.worldY = 0;
        a.direction = "down";

        b.worldX = 500;
        b.worldY = 500;

        Entity[] targets = { b };
        int result = checker.checkEntity(a, targets);

        assertEquals(999, result, "Non-overlapping entities should return 999");
    }

    /**
     * checkEntity should skip null entries in the target array.
     */
    @Test
    public void testCheckEntitySkipsNullEntries() {
        Entity a = makeEntity(gp);
        a.worldX = 100;
        a.worldY = 100;
        a.direction = "down";

        Entity[] targets = { null, null };
        int result = checker.checkEntity(a, targets);

        assertEquals(999, result, "Null entries should be skipped, returning 999");
    }

    /**
     * An entity should not collide with itself.
     */
    @Test
    public void testCheckEntitySkipsSelf() {
        Entity a = makeEntity(gp);
        a.worldX = 100;
        a.worldY = 100;
        a.direction = "down";

        Entity[] targets = { a }; // same reference
        int result = checker.checkEntity(a, targets);

        assertEquals(999, result, "Entity should not collide with itself");
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    /** Creates a minimal Entity with a standard hitbox. */
    private Entity makeEntity(GamePanel gp) {
        Entity e = new Entity(gp) {};
        e.solidArea = new Rectangle(0, 0, 40, 40);
        e.solidAreaDefaultX = 0;
        e.solidAreaDefaultY = 0;
        e.speed = 3;
        e.type = 1;
        return e;
    }
}
