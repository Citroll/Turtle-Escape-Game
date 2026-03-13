package ca.sfu.cmpt276.turtleescape.ai;

/**
 * Represents a single node in the pathfinding grid.
 * Each node corresponds to one tile on the map and stores
 * cost values and state flags used by the A* search algorithm.
 */
public class Node {

    /** The parent node this node was reached from, used to reconstruct the path */
    Node parent;

    /** The row position of this node in the tile grid */
    public int row;

    /** The column position of this node in the tile grid */
    public int col;

    /** The movement cost from the start node to this node */
    int gCost;

    /** The estimated cost from this node to the goal node (heuristic) */
    int hCost;

    /** The total cost: gCost + hCost */
    int fCost;

    /** Whether this node is blocked by a solid tile and cannot be traversed */
    boolean solid;

    /** Whether this node is in the open list and awaiting evaluation */
    boolean open;

    /** Whether this node has already been evaluated */
    boolean checked;

    /**
     * Constructs a Node at the given grid position.
     *
     * @param col the column of this node in the tile grid
     * @param row the row of this node in the tile grid
     */
    public Node(int col, int row){
        this.col = col;
        this.row = row;
    }
}