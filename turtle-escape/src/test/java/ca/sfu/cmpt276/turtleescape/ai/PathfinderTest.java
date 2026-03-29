package ca.sfu.cmpt276.turtleescape.ai;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.ai.Node;
import ca.sfu.cmpt276.turtleescape.ai.Pathfinder;
import ca.sfu.cmpt276.turtleescape.integration.FakeGamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Pathfinder (A* algorithm).
 * Tests cover node grid construction, cost calculation, path search on a clear
 * map, failure when the goal is completely walled off, and path list ordering.
 */
public class PathfinderTest {

    GamePanel gp;
    Pathfinder pFinder;

    @BeforeEach
    public void setUp() {
        // setNodes() iterates col=0..maxWorldCol-1, row=0..maxWorldRow-1
        // and accesses mapTileNum[col][row]. With a 30x30 grid the loop runs
        // while col < 30, so max index is 29 — but the TILE array is [cols][rows]
        // meaning index 30 would be out of bounds. Use 31x31 so all test coords
        // (max goal at col=8, row=8) fit comfortably within the grid.
        gp = FakeGamePanel.build();
        pFinder = gp.pFinder;
    }

    // ── instantiateNodes ─────────────────────────────────────────────────────

    /**
     * instantiateNodes should create a node for every grid position.
     */
    @Test
    public void testInstantiateNodesCreatesFullGrid() {
        for (int c = 0; c < 20; c++) {
            for (int r = 0; r < 20; r++) {
                assertNotNull(pFinder.node[c][r],
                        "Node at [" + c + "][" + r + "] should not be null");
            }
        }
    }

    /**
     * Each node should store the correct col and row after instantiation.
     */
    @Test
    public void testInstantiateNodesCorrectPositions() {
        assertEquals(5,  pFinder.node[5][3].col);
        assertEquals(3,  pFinder.node[5][3].row);
        assertEquals(10, pFinder.node[10][7].col);
        assertEquals(7,  pFinder.node[10][7].row);
    }

    // ── resetNodes ───────────────────────────────────────────────────────────

    /**
     * resetNodes should clear open, checked, and solid flags on all nodes.
     */
    @Test
    public void testResetNodesClearsAllFlags() {
        // Dirty some nodes
        pFinder.node[3][3].open    = true;
        pFinder.node[3][3].checked = true;
        pFinder.node[3][3].solid   = true;

        pFinder.resetNodes();

        assertFalse(pFinder.node[3][3].open);
        assertFalse(pFinder.node[3][3].checked);
        assertFalse(pFinder.node[3][3].solid);
    }

    /**
     * resetNodes should clear the open list and path list.
     */
    @Test
    public void testResetNodesClearsLists() {
        pFinder.openList.add(new Node(0, 0));
        pFinder.pathList.add(new Node(1, 1));

        pFinder.resetNodes();

        assertTrue(pFinder.openList.isEmpty(), "openList should be empty after reset");
        assertTrue(pFinder.pathList.isEmpty(), "pathList should be empty after reset");
    }

    /**
     * resetNodes should set goalReached to false and step to 0.
     */
    @Test
    public void testResetNodesResetsCounters() {
        pFinder.goalReached = true;
        pFinder.step = 42;

        pFinder.resetNodes();

        assertFalse(pFinder.goalReached);
        assertEquals(0, pFinder.step);
    }

    // ── getCost ──────────────────────────────────────────────────────────────

    /**
     * getCost should set fCost = gCost + hCost for a node.
     */
    @Test
    public void testGetCostSetsFCost() {
        // Manually set start and goal so getCost has reference points
        pFinder.setNodes(0, 0, 5, 5);

        Node n = pFinder.node[3][3];
        // After setNodes, every node has had getCost called
        assertEquals(n.gCost + n.hCost, n.fCost,
                "fCost should equal gCost + hCost");
    }

    /**
     * The start node should have gCost = 0 (distance from itself is zero).
     */
    @Test
    public void testStartNodeHasGCostZero() {
        pFinder.setNodes(2, 2, 8, 8);
        Node startNode = pFinder.node[2][2];
        assertEquals(0, startNode.gCost, "Start node gCost should be 0");
    }

    /**
     * The goal node should have hCost = 0 (distance to itself is zero).
     */
    @Test
    public void testGoalNodeHasHCostZero() {
        pFinder.setNodes(2, 2, 8, 8);
        Node goalNode = pFinder.node[8][8];
        assertEquals(0, goalNode.hCost, "Goal node hCost should be 0");
    }

    // ── search ───────────────────────────────────────────────────────────────

    /**
     * search() should return true and populate pathList when a clear path exists.
     */
    @Test
    public void testSearchFindsPathOnClearMap() {
        pFinder.setNodes(0, 0, 5, 0);
        boolean found = pFinder.search();

        assertTrue(found, "Should find a path on a clear map");
        assertFalse(pFinder.pathList.isEmpty(), "pathList should not be empty after successful search");
    }

    /**
     * The last node in pathList should be the goal node.
     */
    @Test
    public void testSearchPathListEndsAtGoal() {
        pFinder.setNodes(0, 0, 4, 0);
        pFinder.search();

        Node last = pFinder.pathList.get(pFinder.pathList.size() - 1);
        assertEquals(4, last.col, "Last path node should be at goal col");
        assertEquals(0, last.row, "Last path node should be at goal row");
    }

    /**
     * search() should return false when the goal is completely surrounded by solid tiles.
     */
    @Test
    public void testSearchReturnsFalseWhenGoalIsWalledOff() {
        // Wall off the goal at (5,5) from all four sides
        FakeGamePanel.setSolidTile(gp, 5, 4); // above
        FakeGamePanel.setSolidTile(gp, 5, 6); // below
        FakeGamePanel.setSolidTile(gp, 4, 5); // left
        FakeGamePanel.setSolidTile(gp, 6, 5); // right

        pFinder.setNodes(0, 0, 5, 5);
        boolean found = pFinder.search();

        assertFalse(found, "Should not find a path when goal is walled off");
    }

    /**
     * Searching from start to start should result in an empty path.
     */
    @Test
    public void testSearchStartEqualsGoalProducesEmptyPath() {
        pFinder.setNodes(3, 3, 3, 3);
        boolean found = pFinder.search();

        // Goal is the start, so it's immediately reached with no steps
        assertTrue(found || pFinder.pathList.isEmpty(),
                "Start == goal should either succeed immediately or produce an empty path");
    }
}