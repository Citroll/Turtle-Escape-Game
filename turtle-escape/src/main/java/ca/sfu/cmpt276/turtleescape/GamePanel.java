package ca.sfu.cmpt276.turtleescape;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the main game panel where all game rendering occurs.
 * Extends JPanel to provide a drawable surface within the game window.
 */
public class GamePanel extends JPanel implements Runnable{

    /** The base tile size in pixels before scaling */
    final int originalTileSize = 16;

    /** Scale factor applied to the original tile size */
    final int scale = 3;

    /** The actual tile size after scaling (48x48 pixels) */
    final int tileSize = originalTileSize * scale;

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

    /**
     * Constructs the GamePanel and initializes display settings.
     * Sets preferred size, background color, and enables double buffering.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); // will call run() method
    }

    @Override
    public void run() {
        /** While this gameThread exists it will repeat the process inside of these brackets*/
        while(gameThread != null){

            // Update: Character position
            update();

            // Update: Redraw screen with new info
            repaint();
        }
    }

    public void update(){

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);

        g2.fillRect(100, 100, tileSize, tileSize);

        g2.dispose();
    }
}