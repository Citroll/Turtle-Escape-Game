package ca.sfu.cmpt276.turtleescape.ai;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Node class.
 * Node is a simple data object used by the A* pathfinder.
 * Tests verify correct construction and default field values.
 */
public class NodeTest {

    /**
     * A node should store the col and row it was constructed with.
     */
    @Test
    public void testNodeStoresColAndRow() {
        Node node = new Node(5, 3);
        assertEquals(5, node.col, "col should be 5");
        assertEquals(3, node.row, "row should be 3");
    }

    /**
     * A newly constructed node should have all cost fields at zero
     * because no search has been run yet.
     */
    @Test
    public void testNewNodeCostsDefaultToZero() {
        Node node = new Node(0, 0);
        assertEquals(0, node.gCost, "gCost should default to 0");
        assertEquals(0, node.hCost, "hCost should default to 0");
        assertEquals(0, node.fCost, "fCost should default to 0");
    }

    /**
     * A newly constructed node should not be open, checked, or solid.
     */
    @Test
    public void testNewNodeFlagsDefaultToFalse() {
        Node node = new Node(2, 7);
        assertFalse(node.open,    "open should default to false");
        assertFalse(node.checked, "checked should default to false");
        assertFalse(node.solid,   "solid should default to false");
    }

    /**
     * A newly constructed node should have no parent.
     */
    @Test
    public void testNewNodeParentIsNull() {
        Node node = new Node(1, 1);
        assertNull(node.parent, "parent should default to null");
    }

    /**
     * Two nodes at different positions should store independent coordinates.
     */
    @Test
    public void testMultipleNodesAreIndependent() {
        Node a = new Node(0, 0);
        Node b = new Node(9, 9);
        assertNotEquals(a.col, b.col);
        assertNotEquals(a.row, b.row);
    }

    /**
     * Node at origin (0,0) should still construct correctly.
     */
    @Test
    public void testNodeAtOrigin() {
        Node node = new Node(0, 0);
        assertEquals(0, node.col);
        assertEquals(0, node.row);
    }
}
