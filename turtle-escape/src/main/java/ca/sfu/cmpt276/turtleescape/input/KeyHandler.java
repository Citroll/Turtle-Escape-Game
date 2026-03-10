package ca.sfu.cmpt276.turtleescape.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input for the game.
 * Implements KeyListener to track which movement keys are currently held down.
 * Movement keys are WASD.
 */
public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    /** Flag for escape key press (used for pausing) */
    public boolean escapePressed;

    /**
     * Not used. Required by KeyListener interface.
     *
     * @param e the key event
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Called when a key is pressed. Sets the corresponding direction flag to true.
     *
     * @param e the key event containing the key code of the pressed key
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); // Gets key number of key pressed and stores it

        if (code == KeyEvent.VK_W) { // If user press W key
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) { // If user press A Key
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) { // If user press S Key
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) { // If user press D Key
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) { // If user press ESC key
            escapePressed = true;
        }
    }

    /**
     * Called when a key is released. Sets the corresponding direction flag to
     * false.
     *
     * @param e the key event containing the key code of the released key
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) { // If user press W key
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) { // If user press A Key
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) { // If user press S Key
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) { // If user press D Key
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ESCAPE) { // If user press ESC key
            escapePressed = false;
        }
    }
}
