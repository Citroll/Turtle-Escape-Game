package ca.sfu.cmpt276.turtleescape.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input for the game.
 * Implements KeyListener to track which movement keys are currently held down.
 * Movement keys are WASD.
 */
public class KeyHandler implements KeyListener {
    // Boolean variables that contain press state of a key
    public boolean upPressed, downPressed, leftPressed, rightPressed, escapePressed;

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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: // If user press W Key
                upPressed = true;
                break;
            case KeyEvent.VK_A: // If user press A Key
                leftPressed = true;
                break;
            case KeyEvent.VK_S: // If user press S Key
                downPressed = true;
                break;
            case KeyEvent.VK_D: // If user press D Key
                rightPressed = true;
                break;
            case KeyEvent.VK_ESCAPE: // If user press ESC key
                escapePressed = true;
                break;
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: // If user release W Key
                upPressed = false;
                break;
            case KeyEvent.VK_A: // If user release A Key
                leftPressed = false;
                break;
            case KeyEvent.VK_S: // If user release S Key
                downPressed = false;
                break;
            case KeyEvent.VK_D: // If user release D Key
                rightPressed = false;
                break;
            case KeyEvent.VK_ESCAPE: // If user release ESC key
                escapePressed = false;
                break;
        }
    }
}
