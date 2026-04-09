package ca.sfu.cmpt276.turtleescape.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import ca.sfu.cmpt276.turtleescape.ai.Pathfinder;
import ca.sfu.cmpt276.turtleescape.audio.AudioManager;
import ca.sfu.cmpt276.turtleescape.audio.Sound;
import ca.sfu.cmpt276.turtleescape.collision.CollisionChecker;
import ca.sfu.cmpt276.turtleescape.entity.Entity;
import ca.sfu.cmpt276.turtleescape.entity.Player;
import ca.sfu.cmpt276.turtleescape.input.KeyHandler;
import ca.sfu.cmpt276.turtleescape.object.AssetSetter;
import ca.sfu.cmpt276.turtleescape.object.IceCreamManager;
import ca.sfu.cmpt276.turtleescape.object.SuperObject;
import ca.sfu.cmpt276.turtleescape.tile.TileManager; 

/**
 * Represents the main game panel where all game rendering and logic occurs.
 * Extends JPanel to provide a drawable surface within the game window.
 * Implements Runnable to support the game loop running on a separate thread.
 */
public class GamePanel extends JPanel implements Runnable, MouseListener, PanelSettings{

    /**
     * Enum representing the different states of the game
     */
    public enum GameState {
        TITLE,
        PLAYING,
        PUNISHED,
        PAUSED,
        DEAD,
        WIN
    }

    /**
     * Current state of the game
     */
    public GameState gameState = GameState.TITLE;

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
    public SuperObject[] obj = new SuperObject[50];

    public Entity enemy[] = new Entity[20];

    /**
     * Handles placing objects onto the map
     */
    public AssetSetter aSetter = new AssetSetter(this);

    /**
     *  Ice cream manager
     */

    public IceCreamManager iceCreamManager = new IceCreamManager(this);

    //(Smell 5+6 fix):
    public AudioManager audio = new AudioManager();


    public Pathfinder pFinder = new Pathfinder(this);

    /**
     * Constructs the GamePanel and initializes display settings. Sets preferred
     * size, background color, enables double buffering, and registers the key
     * listener for input.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
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
        playMusic(Sound.MUSIC);
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
        GameState stateAtStart = gameState;

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
            tileM.update();
        }

        if (gameState == GameState.DEAD && stateAtStart != GameState.DEAD) {
            audio.stopAll();
            playSE(Sound.SFX_DEATH);
        }

        if (gameState == GameState.WIN && stateAtStart != GameState.WIN) {
            stopMoveSE();
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
            ui.drawGreenFlash(g2, getWidth(), getHeight());
        } else if (gameState == GameState.PAUSED) {
            drawGame(g2);
            ui.drawPauseScreen(g2, getWidth(), getHeight());
        } else if (gameState == GameState.DEAD) {
            drawGame(g2);
            ui.drawDeathScreen(g2, getWidth(), getHeight());
        } else if (gameState == GameState.WIN) {
            drawGame(g2);
            ui.drawWinScreen(g2, getWidth(), getHeight());
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

    // ADD delegation methods so existing call sites still work:
    public void playMusic(int i)          { audio.playMusic(i); }
    public void stopMusic()               { audio.stopMusic(); }
    public void playSE(int i)             { audio.playSE(i); }
    public void playSE(int i, float v)    { audio.playSE(i, v); }
    public void playMoveSE(int i)         { audio.playMoveSE(i); }
    public void stopMoveSE()              { audio.stopMoveSE(); }

    /**
     * Resets the game to its initial state and transitions to the playing state.
     * Resets the player's position, score, and invincibility, re-places all
     * objects and enemies, resets the ice cream manager and UI timer.
     */
    public void restartGame() {
        // reset audio state
        audio.stopAll();

        // reset player
        player.setDefaultValues();
        player.score = 0;
        player.invincible = false;
        player.invincibleCounter = 0;

        // reset objects and enemies
        aSetter.setObject();
        aSetter.setMonster();

        // reset ice cream
        iceCreamManager = new IceCreamManager(this);

        // reset UI timer
        ui.playTime = 0;

        // restart background music
        playMusic(Sound.MUSIC);

        gameState = GameState.PLAYING;
    }
}