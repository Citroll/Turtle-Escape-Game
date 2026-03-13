package ca.sfu.cmpt276.turtleescape.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import ca.sfu.cmpt276.turtleescape.ai.Pathfinder;
import ca.sfu.cmpt276.turtleescape.collision.CollisionChecker;
import ca.sfu.cmpt276.turtleescape.entity.Entity;
import ca.sfu.cmpt276.turtleescape.entity.Player;
import ca.sfu.cmpt276.turtleescape.input.KeyHandler;
import ca.sfu.cmpt276.turtleescape.object.AssetSetter;
import ca.sfu.cmpt276.turtleescape.object.IceCreamManager;
import ca.sfu.cmpt276.turtleescape.object.SuperObject;
import ca.sfu.cmpt276.turtleescape.tile.TileManager;
import ca.sfu.cmpt276.turtleescape.Sound; 

/**
 * Represents the main game panel where all game rendering and logic occurs.
 * Extends JPanel to provide a drawable surface within the game window.
 * Implements Runnable to support the game loop running on a separate thread.
 */
public class GamePanel extends JPanel implements Runnable, MouseListener {

    /**
     * Enum representing the different states of the game
     */
    public enum GameState {
        TITLE,
        PLAYING,
        PUNISHED,
        PAUSED
    }

    /**
     * Current state of the game
     */
    public GameState gameState = GameState.TITLE;

    /**
     * Target frames per second for the game loop
     */
    int FPS = 60;

    /**
     * The base tile size in pixels before scaling
     */
    final int originalTileSize = 16;

    /**
     * Scale factor applied to the original tile size
     */
    final int scale = 3;

    /**
     * The actual tile size after scaling (48x48 pixels)
     */
    public final int tileSize = originalTileSize * scale;

    /**
     * Number of tile columns on screen
     */
    public final int maxScreenCol = 16;

    /**
     * Number of tile rows on screen
     */
    public final int maxScreenRow = 12;

    /**
     * Total screen width in pixels
     */
    public final int screenWidth = tileSize * maxScreenCol;

    /**
     * Total screen height in pixels
     */
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 64;
    public final int maxWorldRow = 22;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    /**
     * The thread that runs the game loop. Starting it calls run() automatically
     */
    Thread gameThread;

    /**
     * Handles keyboard input from the player
     */
    KeyHandler keyH = new KeyHandler();

    /**
     * The player-controlled turtle entity
     */
    public Player player = new Player(this, keyH);

    /**
     * Manages loading and rendering of all map tiles
     */
    public TileManager tileM = new TileManager(this);

    /**
     * UI for managing score and time display
     */
    public UI ui = new UI(this);

    /**
     * Collision checker for player collisions
     */
    public CollisionChecker cChecker = new CollisionChecker(this);

    /**
     * Array of interactive objects on the map (rewards, items). Max 10 at once.
     */
    public SuperObject[] obj = new SuperObject[10];

    public Entity enemy[] = new Entity[20];

    /**
     * Handles placing objects onto the map
     */
    public AssetSetter aSetter = new AssetSetter(this);

    /**
     *  Ice cream manager
     */

    public IceCreamManager iceCreamManager = new IceCreamManager(this);
     
    /**
     * instance for bg music
     */
    Sound music = new Sound();
    
    /**
     * sound effect instance (one shots)
     */
    Sound se = new Sound();

    /**
     * move sound effect
     */
    Sound moveSE = new Sound();

    public Pathfinder pFinder = new Pathfinder(this);

    /**
     * Constructs the GamePanel and initializes display settings. Sets preferred
     * size, background color, enables double buffering, and registers the key
     * listener for input.
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
     * Sets up the game by placing all objects on the map. Should be called
     * before starting the game thread.
     */
    public void setupGame() {
        aSetter.setObject();
        aSetter.setMonster();
        playMusic(0);
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
     * Caps the frame rate at {@code FPS} by sleeping for the remaining time in
     * each frame interval. Each iteration updates game state and repaints the
     * screen.
     */
    @Override
    public void run() {

        double drawInterval = 1_000_000_000.0 / FPS; // 1 60th of a second
        double nextDrawTime = System.nanoTime() + drawInterval; // Draw the screen after 1/60th of a second

        /**
         * While this gameThread exists it will repeat the process inside of
         * these brackets
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
     * Updates the game state for the current frame. Calls update on the player
     * to process movement input. Will be extended to update enemies, score,
     * timers, etc.
     */
    public void update() {
        if (keyH.escapePressed && gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
            keyH.escapePressed = false;
        }
    
        if (gameState == GameState.PLAYING || gameState == GameState.PUNISHED) {
            player.update();

            for (int i = 0; i < enemy.length; i++) {
                if(enemy[i] != null){
                    enemy[i].update();
                }
            }

            iceCreamManager.update();
        }
    }

    /**
     * Marks an ice cream slot as permanently collected so it won't respawn.
     *
     * @param slot the obj array index
     */
    public void setIceCreamCollected(int slot) {
        iceCreamManager.setCollected(slot);
    }

    /**
     * Renders all game elements to the screen. Draws the tile map first, then
     * the player on top. Called automatically by repaint() each frame.
     *
     * @param g the Graphics context provided by Swing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (gameState == GameState.TITLE) {
            ui.drawTitleScreen(g2, getWidth(), getHeight());
        } else if (gameState == GameState.PLAYING || gameState == GameState.PUNISHED) {
            drawGame(g2);
            ui.drawRedFlash(g2, getWidth(), getHeight());
        } else if (gameState == GameState.PAUSED) {
            drawGame(g2);
            ui.drawPauseScreen(g2, getWidth(), getHeight());
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

        for (int i = 0; i < enemy.length; i++) {
            if (enemy[i] != null) {
                enemy[i].draw(g2);
            }
        }

        player.draw(g2);

        ui.draw(g2, gameState, getWidth(), getHeight());
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

    /**
     * plays background music from the sound url array and loops it continuously.
     *
     * @param i the index of the music track in the sound url array
     */
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
        music.setVolume(0.72f);
    }

    /**
     * stops the currently playing background music.
     */
    public void stopMusic() {
        music.stop();
    }

    /**
     * plays a one-shot sound effect from the sound url array.
     *
     * @param i the index of the sound effect in the sound url array
     */
    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

    /**
     * plays a looping movement sound effect (walk/swim) from the sound url array.
     *
     * @param i the index of the movement sound in the sound url array
     */
    public void playMoveSE(int i) {
        moveSE.setFile(i);
        moveSE.play();
        moveSE.loop();
    }

    /**
     * stops the currently looping movement sound effect.
     */
    public void stopMoveSE() {
        moveSE.stop();
    }
}
