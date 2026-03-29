package ca.sfu.cmpt276.turtleescape.integration;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.entity.Entity;
import ca.sfu.cmpt276.turtleescape.integration.FakeGamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for tile collision detection.
 * Tests CollisionChecker.checkTile() wired to the real TileManager grid.
 * Solid tiles block movement (collisionOn=true); walkable tiles do not.
 * Also verifies boundary clamping prevents ArrayIndexOutOfBoundsExceptions.
 *
 * Grid is 25x25 tiles (tileSize=48px).
 * Entity has solidArea(0,0,40,40) and speed=3.
 *
 * Key geometry for "right":
 *   entityRightX  = worldX + 0 + 40 = worldX + 40
 *   entityRightCol = (worldX+40) / 48   (same tile as worldX if worldX%48==0)
 *   after speed:   (worldX+40+3) / 48
 *   → to land in col N+1, need worldX such that (worldX+43)/48 >= N+1
 *   With worldX = N*48: (N*48+43)/48 = N  (stays in col N, never N+1)
 *   With worldX = N*48+5: (N*48+5+43)/48 = (N*48+48)/48 = N+1  ✓
 *
 * Key geometry for "down":
 *   entityBottomY  = worldY + 0 + 40 = worldY + 40
 *   entityBottomRow = (worldY+40) / 48
 *   after speed:    (worldY+40+3) / 48
 *   With worldY = N*48+5: (N*48+5+43)/48 = N+1  ✓
 */
public class TileCollisionTest {

    GamePanel gp;

    @BeforeEach
    public void setUp() {
        // 25x25 grid gives room for all entity positions + boundary tests
        gp = FakeGamePanel.build();
    }

    // ── moving up ────────────────────────────────────────────────────────────

    /**
     * Entity at tile (5,5) moving up: top edge is at worldY=5*48=240,
     * minus speed (3) = 237 → row 237/48 = 4. Tile (5,4) is solid → blocked.
     */
    @Test
    public void testSolidTileAboveBlocksUpwardMovement() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * gp.tileSize;
        entity.worldY = 5 * gp.tileSize;
        entity.direction = "up";
        entity.collisionOn = false;

        FakeGamePanel.setSolidTile(gp, 5, 4);

        gp.cChecker.checkTile(entity);

        assertTrue(entity.collisionOn, "Solid tile above should block upward movement");
    }

    /**
     * Walkable tile above should not block upward movement.
     */
    @Test
    public void testWalkableTileAboveDoesNotBlockUpwardMovement() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * gp.tileSize;
        entity.worldY = 5 * gp.tileSize;
        entity.direction = "up";
        entity.collisionOn = false;

        FakeGamePanel.setWalkableTile(gp, 5, 4);

        gp.cChecker.checkTile(entity);

        assertFalse(entity.collisionOn, "Walkable tile above should not block movement");
    }

    // ── moving down ──────────────────────────────────────────────────────────

    /**
     * Entity at worldY = 5*48+5 = 245. bottomY = 245+40 = 285. bottomRow+speed = (285+3)/48 = 6.
     * Tile at row 6 is solid → blocked.
     */
    @Test
    public void testSolidTileBelowBlocksDownwardMovement() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * 48;
        entity.worldY = 5 * 48 + 6; // nudge so bottom+speed crosses into row 6
        entity.direction = "down";
        entity.collisionOn = false;

        FakeGamePanel.setSolidTile(gp, 5, 6);
        FakeGamePanel.setSolidTile(gp, 6, 6); // cover right column too

        gp.cChecker.checkTile(entity);

        assertTrue(entity.collisionOn, "Solid tile below should block downward movement");
    }

    /**
     * Walkable tile below should not block downward movement.
     */
    @Test
    public void testWalkableTileBelowDoesNotBlockDownwardMovement() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * gp.tileSize;
        entity.worldY = 5 * gp.tileSize;
        entity.direction = "down";
        entity.collisionOn = false;

        FakeGamePanel.setWalkableTile(gp, 5, 6);

        gp.cChecker.checkTile(entity);

        assertFalse(entity.collisionOn, "Walkable tile below should not block movement");
    }

    // ── moving left ──────────────────────────────────────────────────────────

    /**
     * Entity at tile (5,5) moving left: leftX = 5*48=240, minus speed(3)=237 → col 4. Solid → blocked.
     */
    @Test
    public void testSolidTileLeftBlocksLeftwardMovement() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * gp.tileSize;
        entity.worldY = 5 * gp.tileSize;
        entity.direction = "left";
        entity.collisionOn = false;

        FakeGamePanel.setSolidTile(gp, 4, 5);

        gp.cChecker.checkTile(entity);

        assertTrue(entity.collisionOn, "Solid tile to the left should block leftward movement");
    }

    /**
     * Walkable tile to the left should not block leftward movement.
     */
    @Test
    public void testWalkableTileLeftDoesNotBlockLeftwardMovement() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * gp.tileSize;
        entity.worldY = 5 * gp.tileSize;
        entity.direction = "left";
        entity.collisionOn = false;

        FakeGamePanel.setWalkableTile(gp, 4, 5);

        gp.cChecker.checkTile(entity);

        assertFalse(entity.collisionOn, "Walkable tile to the left should not block movement");
    }

    // ── moving right ─────────────────────────────────────────────────────────

    /**
     * Entity at worldX = 5*48+5 = 245. rightX = 245+40 = 285. rightCol+speed = (285+3)/48 = 6.
     * Tile at col 6 is solid → blocked.
     */
    @Test
    public void testSolidTileRightBlocksRightwardMovement() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * 48 + 6; // nudge so right+speed crosses into col 6
        entity.worldY = 5 * 48;
        entity.direction = "right";
        entity.collisionOn = false;

        FakeGamePanel.setSolidTile(gp, 6, 5);
        FakeGamePanel.setSolidTile(gp, 6, 6); // cover bottom row too

        gp.cChecker.checkTile(entity);

        assertTrue(entity.collisionOn, "Solid tile to the right should block rightward movement");
    }

    /**
     * Walkable tile to the right should not block rightward movement.
     */
    @Test
    public void testWalkableTileRightDoesNotBlockRightwardMovement() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * gp.tileSize;
        entity.worldY = 5 * gp.tileSize;
        entity.direction = "right";
        entity.collisionOn = false;

        FakeGamePanel.setWalkableTile(gp, 6, 5);

        gp.cChecker.checkTile(entity);

        assertFalse(entity.collisionOn, "Walkable tile to the right should not block movement");
    }

    // ── boundary clamping ─────────────────────────────────────────────────────

    /**
     * Entity at the top edge of the map moving up should not throw AIOOBE.
     */
    @Test
    public void testNoExceptionAtTopMapBoundary() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * gp.tileSize;
        entity.worldY = 0;
        entity.direction = "up";

        assertDoesNotThrow(() -> gp.cChecker.checkTile(entity),
                "checkTile should not throw at the top map boundary");
    }

    /**
     * Entity positioned so its bottom edge is safely within the grid, moving down.
     * Should not throw AIOOBE — clamping keeps the row in bounds.
     */
    @Test
    public void testNoExceptionAtBottomMapBoundary() {
        Entity entity = makeEntity(gp);
        entity.worldX = 5 * gp.tileSize;
        // Place near bottom but leave room so solidArea stays in grid:
        // row = (worldY+40+3)/48 must be < maxWorldRow(25), so worldY < 25*48-43 = 1157
        entity.worldY = 19 * 48; // row 19, bottom+speed = row 19 (within 22 rows)
        entity.direction = "down";

        assertDoesNotThrow(() -> gp.cChecker.checkTile(entity),
                "checkTile should not throw at the bottom map boundary");
    }

    /**
     * Entity at the left edge of the map moving left should not throw AIOOBE.
     */
    @Test
    public void testNoExceptionAtLeftMapBoundary() {
        Entity entity = makeEntity(gp);
        entity.worldX = 0;
        entity.worldY = 5 * gp.tileSize;
        entity.direction = "left";

        assertDoesNotThrow(() -> gp.cChecker.checkTile(entity));
    }

    /**
     * Entity near the right edge of the map moving right should not throw AIOOBE.
     */
    @Test
    public void testNoExceptionAtRightMapBoundary() {
        Entity entity = makeEntity(gp);
        // Keep rightX + speed within clamped range
        entity.worldX = 90 * 48; // col 90, right+speed = col 90 (well within 100 cols)
        entity.worldY = 5 * gp.tileSize;
        entity.direction = "right";

        assertDoesNotThrow(() -> gp.cChecker.checkTile(entity));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Entity makeEntity(GamePanel gp) {
        Entity e = new Entity(gp) {};
        e.solidArea         = new Rectangle(0, 0, 40, 40);
        e.solidAreaDefaultX = 0;
        e.solidAreaDefaultY = 0;
        e.speed             = 3;
        return e;
    }
}