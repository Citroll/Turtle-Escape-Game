package ca.sfu.cmpt276.turtleescape;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;

import javax.swing.*;

/**
 * Entry point for the Turtle Escape application.
 * Initializes the game window and launches the game panel.
 */
public class Main {

    /**
     * Main method that creates and displays the game window.
     * Sets up the JFrame with the GamePanel and makes it visible.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        JFrame window = new JFrame();

        // Allow window to be closed when user presses 'x' button
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Turtle Escape");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        // Set pop up location to the center of the screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}