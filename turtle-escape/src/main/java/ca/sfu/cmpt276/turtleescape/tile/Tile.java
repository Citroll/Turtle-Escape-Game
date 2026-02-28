package ca.sfu.cmpt276.turtleescape.tile;

import java.awt.image.BufferedImage;

/**
 * Represents a single tile type in the game world.
 * Stores the tile's visual image and whether it blocks movement.
 */
public class Tile {

    /** The image used to render this tile on screen */
    public BufferedImage image;

    /** Whether this tile blocks entity movement. False by default (walkable) */
    public boolean collision = false;
}
