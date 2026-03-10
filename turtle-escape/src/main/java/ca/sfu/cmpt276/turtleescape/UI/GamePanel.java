package ca.sfu.cmpt276.turtleescape.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import ca.sfu.cmpt276.turtleescape.entity.Player;
import ca.sfu.cmpt276.turtleescape.input.KeyHandler;
import ca.sfu.cmpt276.turtleescape.collision.CollisionChecker;
import ca.sfu.cmpt276.turtleescape.object.AssetSetter;
import ca.sfu.cmpt276.turtleescape.object.OBJ_IceCream;
import ca.sfu.cmpt276.turtleescape.object.SuperObject;
import ca.sfu.cmpt276.turtleescape.tile.TileManager;

/**
 * Represents the main game panel where all game rendering and logic occurs.
 * Extends JPanel to provide a drawable surface within the game window.
 * Implements Runnable to support the game loop running on a separate thread.
 */
public class GamePanel extends JPanel implements Runnable, MouseListener {

    /** Enum representing the different states of the game */
    public enum GameState {
        TITLE,
        PLAYING
    }

    /** Current state of the game */
    public GameState gameState = GameState.TITLE;

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
    public TileManager tileM = new TileManager(this);

    /** UI for managing score and time display */
    public UI ui = new UI(this);

    public CollisionChecker cChecker = new CollisionChecker(this);

    /** Array of interactive objects on the map (rewards, items). Max 10 at once. */
    public SuperObject[] obj = new SuperObject[10];

    /** Handles placing objects onto the map */
    public AssetSetter aSetter = new AssetSetter(this);

    // --- Ice cream spawn/despawn config ---
    /** Frames between spawn attempts (~5 seconds at 60fps) */
    private static final int ICE_CREAM_SPAWN_INTERVAL = 300;
    /** How many frames ice cream stays before vanishing (~3 seconds) */
    private static final int ICE_CREAM_LIFETIME = 180;
    /** Frame counter for spawn timing */
    private int iceCreamSpawnTimer = 0;
    /** Remaining lifetime for ice cream slot 4 (kid at col24/row5) */
    private int iceCreamLife4 = 0;
    /** Remaining lifetime for ice cream slot 5 (kid at col28/row14) */
    private int iceCreamLife5 = 0;
    /** True once ice cream 4 has been collected — won't respawn */
    private boolean iceCream4Collected = false;
    /** True once ice cream 5 has been collected — won't respawn */
    private boolean iceCream5Collected = false;

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
        this.addMouseListener(this);
        this.setFocusable(true);
    }

    /**
     * Sets up the game by placing all objects on the map.
     * Should be called before starting the game thread.
     */
    public void setupGame() {
        aSetter.setObject();
    }

    /**
     * Creates and starts the game thread, which triggers the run() method.
     * Should be called after the game window is made visible.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); // will call run() method
    }

    /**
     * The main game loop. Runs continuously while the game thread is active.
     * Caps the frame rate at {@code FPS} by sleeping for the remaining time
     * in each frame interval. Each iteration updates game state and repaints the
     * screen.
     */
    @Override
    public void run() {

        double drawInterval = 1_000_000_000.0 / FPS; // 1 60th of a second
        double nextDrawTime = System.nanoTime() + drawInterval; // Draw the screen after 1/60th of a second

        /**
         * While this gameThread exists it will repeat the process inside of these
         * brackets
         */
        while (gameThread != null) {

            long currentTime = System.nanoTime();

            // Update: Character position
            update();

            // Update: Redraw screen with new info
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime(); // How much time until nextDrawTime
                remainingTime = remainingTime / 1000000; // convert to milliseconds for sleep method

                if (remainingTime < 0) {
                    remainingTime = 0; // Just in case it uses more time than drawInterval
                }

                Thread.sleep((long) remainingTime); // Sleep for remaining time

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
    public void update() {
        if (gameState == GameState.PLAYING) {
            player.update();
            updateIceCreamSpawns();
        }
    }

    /**
     * Marks an ice cream slot as permanently collected so it won't respawn.
     *
     * @param slot the obj array index (4 or 5)
     */
    public void setIceCreamCollected(int slot) {
        if (slot == 4)
            iceCream4Collected = true;
        if (slot == 5)
            iceCream5Collected = true;
    }

    /**
     * Handles random spawning and timed despawning of ice cream below kid tiles.
     * Ice cream appears one tile below each kid, stays for a few seconds, then
     * vanishes.
     */
    private void updateIceCreamSpawns() {
        iceCreamSpawnTimer++;

        // Try to spawn ice cream every SPAWN_INTERVAL frames
        if (iceCreamSpawnTimer >= ICE_CREAM_SPAWN_INTERVAL) {
            iceCreamSpawnTimer = 0;

            // 50% chance to spawn below kid 1 (col 24, row 5 → ice cream at row 6)
            if (obj[4] == null && !iceCream4Collected && Math.random() < 0.5) {
                obj[4] = new OBJ_IceCream();
                obj[4].worldX = 24 * tileSize;
                obj[4].worldY = 6 * tileSize;
                iceCreamLife4 = ICE_CREAM_LIFETIME;
            }

            // 50% chance to spawn below kid 2 (col 28, row 14 → ice cream at row 15)
            if (obj[5] == null && !iceCream5Collected && Math.random() < 0.5) {
                obj[5] = new OBJ_IceCream();
                obj[5].worldX = 28 * tileSize;
                obj[5].worldY = 15 * tileSize;
                iceCreamLife5 = ICE_CREAM_LIFETIME;
            }
        }

        // Count down and despawn if time runs out
        if (obj[4] != null) {
            iceCreamLife4--;
            if (iceCreamLife4 <= 0) {
                obj[4] = null;
            }
        }
        if (obj[5] != null) {
            iceCreamLife5--;
            if (iceCreamLife5 <= 0) {
                obj[5] = null;
            }
        }
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

        Graphics2D g2 = (Graphics2D) g;

        if (gameState == GameState.TITLE) {
            ui.drawTitleScreen(g2, getWidth(), getHeight());
        } else if (gameState == GameState.PLAYING) {
            drawGame(g2);
        }

        g2.dispose();
    }

    /**
     * Draws the main game elements: tiles, objects, player, and UI.
     *
     * @param g2 the Graphics2D context used for rendering
     */
    private void drawGame(Graphics2D g2) {
        tileM.draw(g2);

        // Draw objects (between tiles and player so player walks over them)
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        player.draw(g2);

        ui.draw(g2);
    }

    // MouseListener methods
    @Override
    public void mouseClicked(MouseEvent e) {
        ui.handleMouseClick(e, gameState);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}