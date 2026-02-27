package ca.sfu.cmpt276.turtleescape.UI;

import ca.sfu.cmpt276.turtleescape.entity.Player;
import ca.sfu.cmpt276.turtleescape.input.KeyHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the main game panel where all game rendering occurs.
 * Extends JPanel to provide a drawable surface within the game window.
 */
public class GamePanel extends JPanel implements Runnable{

    // FPS limit
    int FPS = 60;

    /** The base tile size in pixels before scaling */
    final int originalTileSize = 16;

    /** Scale factor applied to the original tile size */
    final int scale = 3;

    /** The actual tile size after scaling (48x48 pixels) */
    public final int tileSize = originalTileSize * scale;

    /** Number of tile columns on screen */
    final int maxScreenCol = 16;

    /** Number of tile rows on screen */
    final int maxScreenRow = 12;

    /** Total screen width in pixels */
    final int screenWidth = tileSize * maxScreenCol;

    /** Total screen height in pixels */
    final int screenHeight = tileSize * maxScreenRow;

    /** When we start the gameThread it will automatically call run() method*/
    Thread gameThread;

    /** Make a new KeyHandler to listen for keyboard input*/
    KeyHandler keyH = new KeyHandler();

    Player player = new Player(this, keyH);

    /** Set player's default position*/
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    /**
     * Constructs the GamePanel and initializes display settings.
     * Sets preferred size, background color, enables double buffering, and adds a key listener
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); // will call run() method
    }

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
     * When Player presses WASD the player's coordinate increases/decreases by 4 pixels = playerSpeed
     * Then key input is caught by keyHandler and then the update method updates the player coordinates in the game loop
     * Then the repaint method is called to redraw the player in the new position
     * */
    public void update(){
       player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        player.draw(g2);

        g2.dispose();
    }
}