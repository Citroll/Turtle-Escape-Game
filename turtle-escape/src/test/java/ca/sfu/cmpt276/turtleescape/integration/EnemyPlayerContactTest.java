package ca.sfu.cmpt276.turtleescape.integration;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for enemy-player contact detection.
 * Tests the full pipeline: CollisionChecker.checkPlayer() wired to
 * Entity.checkCollision() → GamePanel.GameState.DEAD.
 * Verifies all directions of approach and invincibility protection.
 */
public class EnemyPlayerContactTest {

    GamePanel gp;

    @BeforeEach
    public void setUp() {
        gp = FakeGamePanel.build();
        gp.gameState = GamePanel.GameState.PLAYING;
    }

    /**
     * An enemy moving down and overlapping the player should trigger DEAD state.
     */
    @Test
    public void testEnemyMovingDownKillsPlayer() {
        Entity enemy = makeEnemy(gp);
        // Place enemy just above the player, moving down into them
        enemy.worldX = gp.player.worldX;
        enemy.worldY = gp.player.worldY - 4; // slight offset so speed prediction overlaps
        enemy.direction = "down";

        gp.enemy[0] = enemy;
        enemy.checkCollision();

        assertEquals(GamePanel.GameState.DEAD, gp.gameState,
                "Enemy moving down into player should trigger DEAD");
    }

    /**
     * An enemy moving up and overlapping the player should trigger DEAD state.
     */
    @Test
    public void testEnemyMovingUpKillsPlayer() {
        Entity enemy = makeEnemy(gp);
        enemy.worldX = gp.player.worldX;
        enemy.worldY = gp.player.worldY + 4;
        enemy.direction = "up";

        gp.enemy[0] = enemy;
        enemy.checkCollision();

        assertEquals(GamePanel.GameState.DEAD, gp.gameState);
    }

    /**
     * An enemy moving right and overlapping the player should trigger DEAD state.
     */
    @Test
    public void testEnemyMovingRightKillsPlayer() {
        Entity enemy = makeEnemy(gp);
        enemy.worldX = gp.player.worldX - 4;
        enemy.worldY = gp.player.worldY;
        enemy.direction = "right";

        gp.enemy[0] = enemy;
        enemy.checkCollision();

        assertEquals(GamePanel.GameState.DEAD, gp.gameState);
    }

    /**
     * An enemy moving left and overlapping the player should trigger DEAD state.
     */
    @Test
    public void testEnemyMovingLeftKillsPlayer() {
        Entity enemy = makeEnemy(gp);
        enemy.worldX = gp.player.worldX + 4;
        enemy.worldY = gp.player.worldY;
        enemy.direction = "left";

        gp.enemy[0] = enemy;
        enemy.checkCollision();

        assertEquals(GamePanel.GameState.DEAD, gp.gameState);
    }

    /**
     * An enemy far away from the player should NOT trigger DEAD state.
     */
    @Test
    public void testEnemyFarAwayDoesNotKillPlayer() {
        Entity enemy = makeEnemy(gp);
        enemy.worldX = gp.player.worldX + 10 * gp.tileSize;
        enemy.worldY = gp.player.worldY;
        enemy.direction = "down";

        gp.enemy[0] = enemy;
        enemy.checkCollision();

        assertNotEquals(GamePanel.GameState.DEAD, gp.gameState,
                "Enemy far away should not kill player");
    }

    /**
     * An enemy touching an invincible player should NOT trigger DEAD state.
     */
    @Test
    public void testEnemyDoesNotKillInvinciblePlayer() {
        gp.player.invincible = true;

        Entity enemy = makeEnemy(gp);
        enemy.worldX = gp.player.worldX;
        enemy.worldY = gp.player.worldY;
        enemy.direction = "down";

        gp.enemy[0] = enemy;
        enemy.checkCollision();

        assertNotEquals(GamePanel.GameState.DEAD, gp.gameState,
                "Invincible player should not die on enemy contact");
    }

    /**
     * A non-enemy entity (type != 1) touching the player should NOT trigger DEAD.
     */
    @Test
    public void testNonEnemyEntityDoesNotKillPlayer() {
        Entity npc = makeEnemy(gp);
        npc.type = 0; // not a monster
        npc.worldX = gp.player.worldX;
        npc.worldY = gp.player.worldY;
        npc.direction = "down";

        gp.enemy[0] = npc;
        npc.checkCollision();

        assertNotEquals(GamePanel.GameState.DEAD, gp.gameState,
                "Non-enemy entity should not kill player");
    }

    /**
     * checkPlayerPassive (raw overlap check) should detect contact even
     * when neither entity is moving.
     */
    @Test
    public void testPassiveCheckDetectsStationaryOverlap() {
        Entity enemy = makeEnemy(gp);
        // Place exactly on the player with no movement offset
        enemy.worldX = gp.player.worldX;
        enemy.worldY = gp.player.worldY;
        enemy.direction = "down";

        boolean contact = gp.cChecker.checkPlayer(enemy);
        assertTrue(contact, "Passive check should detect stationary overlap");
    }

    /**
     * checkPlayerPassive should return false when entities are not overlapping.
     */
    @Test
    public void testPassiveCheckReturnsFalseWhenNotOverlapping() {
        Entity enemy = makeEnemy(gp);
        enemy.worldX = gp.player.worldX + 500;
        enemy.worldY = gp.player.worldY + 500;
        enemy.direction = "down";

        boolean contact = gp.cChecker.checkPlayer(enemy);
        assertFalse(contact, "Passive check should return false when entities are far apart");
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Entity makeEnemy(GamePanel gp) {
        Entity e = new Entity(gp) {};
        e.solidArea = new Rectangle(0, 0, 40, 40);
        e.solidAreaDefaultX = 0;
        e.solidAreaDefaultY = 0;
        e.speed = 3;
        e.type = 1; // monster
        e.direction = "down";
        return e;
    }
}
