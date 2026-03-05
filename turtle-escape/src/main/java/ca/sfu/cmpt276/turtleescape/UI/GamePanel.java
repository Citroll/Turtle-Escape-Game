package ca.sfu.cmpt276.turtleescape.UI;

import ca.sfu.cmpt276.turtleescape.entity.Player;
import ca.sfu.cmpt276.turtleescape.input.KeyHandler;
import ca.sfu.cmpt276.turtleescape.tile.TileManager;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the main game panel where all game rendering and logic occurs.
 * Extends JPanel to provide a drawable surface within the game window.
 * Implements Runnable to support the game loop running on a separate thread.
 */
public class GamePanel extends JPanel implements Runnable{

    /** Target frames per second for the game loop */
    int FPS = 60;

    /** The base tile size in pixels before scaling */
    final int originalTileSize = 16;

    /** Scale factor applied to the original tile size */
    final int scale = 3;

    /** The actual tile size after scaling (48x48 pixels) */
    public final int tileSize = originalTileSize * scale;

    /** Number of tile columns on screen */
    public final int maxScreenCol = 16;

    /** Number of tile rows on screen */
    public final int maxScreenRow = 12;

    /** Total screen width in pixels */
    public final int screenWidth = tileSize * maxScreenCol;

    /** Total screen height in pixels */
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 64;
    public final int maxWorldRow = 22;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    /** The thread that runs the game loop. Starting it calls run() automatically */
    Thread gameThread;

    /** Handles keyboard input from the player */
    KeyHandler keyH = new KeyHandler();

    /** The player-controlled turtle entity */
    public Player player = new Player(this, keyH);

    /** Manages loading and rendering of all map tiles */
    TileManager tileM = new TileManager(this);

    /**
     * Constructs the GamePanel and initializes display settings.
     * Sets preferred size, background color, enables double buffering,
     * and registers the key listener for input.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    /**
     * Creates and starts the game thread, which triggers the run() method.
     * Should be called after the game window is made visible.
     */
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); // will call run() method
    }

    /**
     * The main game loop. Runs continuously while the game thread is active.
     * Caps the frame rate at {@code FPS} by sleeping for the remaining time
     * in each frame interval. Each iteration updates game state and repaints the screen.
     */
    @Override
    public void run() {

        double drawInterval = 1_000_000_000.0/FPS; // 1 60th of a second
        double nextDrawTime = System.nanoTime() + drawInterval; // Draw the screen after 1/60th of a second


        /** While this gameThread exists it will repeat the process inside of these brackets*/
        while(gameThread != null){

            long currentTime = System.nanoTime();

            // Update: Character position
            update();

            // Update: Redraw screen with new info
            repaint();


            try {
                double remainingTime = nextDrawTime - System.nanoTime(); // How much time until nextDrawTime
                remainingTime = remainingTime/1000000; // convert to milliseconds for sleep method

                if(remainingTime < 0) {
                    remainingTime = 0; // Just in case it uses more time than drawInterval
                }

                Thread.sleep((long)remainingTime); // Sleep for remaining time

                nextDrawTime += drawInterval; // Add another 1/60th of a second for the loop

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Updates the game state for the current frame.
     * Calls update on the player to process movement input.
     * Will be extended to update enemies, score, timers, etc.
     */
    public void update(){
       player.update();
    }

    /**
     * Renders all game elements to the screen.
     * Draws the tile map first, then the player on top.
     * Called automatically by repaint() each frame.
     *
     * @param g the Graphics context provided by Swing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);

        player.draw(g2);

        g2.dispose();
    }
}