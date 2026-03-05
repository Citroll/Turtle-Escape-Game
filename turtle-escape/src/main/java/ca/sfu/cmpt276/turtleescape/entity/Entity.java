package ca.sfu.cmpt276.turtleescape.entity;

import java.awt.image.BufferedImage;

/**
 * Base class for all entities in the game (player, enemies, NPCs).
 * Stores common properties such as position, speed, direction, and sprite images.
 */
public class Entity {

    /** The x-coordinate of the entity on the screen in pixels */
    public int worldX, worldY;

    /** The movement speed of the entity in pixels per update */
    public int speed;

    /** Sprite images for each direction of movement (2 frames each for animation) */
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    /** The current direction the entity is facing ("up", "down", "left", "right") */
    public String direction;

    /** Tracks how many frames have passed since the last sprite swap */
    public int spriteCounter = 0;

    /** The current sprite frame number (1 or 2) used for walking animation */
    public int spriteNum = 1;
}
