package ca.sfu.cmpt276.turtleescape.ai;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

import java.util.ArrayList;

/**
 * Implements the A* pathfinding algorithm for enemy navigation.
 * Builds a grid of nodes matching the tile map and searches for the
 * shortest walkable path from a start tile to a goal tile each time it is called.
 */
public class Pathfinder {

    /** Reference to the game panel for accessing the tile map dimensions and layout */
    GamePanel gp;

    /** 2D grid of nodes corresponding to each tile in the world map */
    Node[][] node;

    /** List of nodes currently open for evaluation during the search */
    ArrayList<Node> openList = new ArrayList<>();

    /** The ordered list of nodes forming the path from start to goal */
    public ArrayList<Node> pathList = new ArrayList<>();

    /** The node the entity is starting from */
    Node startNode;

    /** The node the entity is trying to reach */
    Node goalNode;

    /** The node currently being evaluated */
    Node currentNode;

    /** True once the goal node has been reached during search */
    boolean goalReached;

    /** Counts iterations to prevent infinite loops during search */
    int step = 0;

    /**
     * Constructs a Pathfinder and initialises the node grid for the world map.
     *
     * @param gp the GamePanel used to access tile map dimensions and collision data
     */
    public Pathfinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    /**
     * Creates a Node object for every tile position in the world map grid.
     */
    public void instantiateNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Resets all nodes in the grid back to their default state.
     * Clears open, checked, and solid flags, empties the open and path lists,
     * and resets the goal and step counters. Must be called before each new search.
     */
    public void resetNodes() {
        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            // Reset open, checked, and solid state
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        //Reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    /**
     * Prepares the node grid for a new search by resetting all nodes,
     * marking solid tiles as impassable, and calculating cost values for each node.
     *
     * @param startCol the column of the starting tile
     * @param startRow the row of the starting tile
     * @param goalCol  the column of the goal tile
     * @param goalRow  the row of the goal tile
     */
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            int tileNum = gp.tileM.mapTileNum[col][row];
            if (gp.tileM.tile[tileNum].collision == true) {
                node[col][row].solid = true;
            }
            // also block on solid overlay tiles
            int overlayNum = gp.tileM.mapOverlayNum[col][row];
            if (overlayNum != -1 && gp.tileM.tile[overlayNum] != null && gp.tileM.tile[overlayNum].collision) {
                node[col][row].solid = true;
            }

            // Set Cost
            getCost(node[col][row]);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }

        }

    }

    /**
     * Calculates and sets the gCost, hCost, and fCost for the given node
     * based on its distance from the start and goal nodes.
     *
     * @param node the node to calculate costs for
     */
    public void getCost(Node node) {
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;
    }

    /**
     * Runs the A* search algorithm from the start node to the goal node.
     * Evaluates neighbouring nodes by fCost each step, up to a maximum of 500 steps.
     * If the goal is reached, calls {@link #trackThePath()} to build the path list.
     *
     * @return true if a path to the goal was found, false otherwise
     */
    public boolean search(){
        while(goalReached == false && step < 500){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;
            openList.remove(currentNode);

            if(row - 1 >= 0){
                openNode(node[col][row - 1]);
            }

            if(col - 1 >= 0){
                openNode(node[col - 1][row]);
            }

            if(row + 1 < gp.maxWorldRow) {
                openNode(node[col][row + 1]);
            }

            if(col + 1 < gp.maxWorldCol){
                openNode(node[col + 1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }
            if(openList.isEmpty()){
                break;
            }

            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }

    /**
     * Adds a node to the open list for evaluation if it has not already been
     * opened, checked, or marked as solid.
     *
     * @param node the node to consider opening
     */
    public void openNode(Node node){
        if(node.open == false && node.checked == false && node.solid == false){
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    /**
     * Reconstructs the path from the goal node back to the start node
     * by following each node's parent reference, and stores it in {@code pathList}
     * in order from start to goal.
     */
    public void trackThePath() {
        Node current = goalNode;

        while(current != startNode){
            pathList.add(0, current);
            current = current.parent;

        }
    }
}