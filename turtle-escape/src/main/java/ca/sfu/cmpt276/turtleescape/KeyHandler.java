package ca.sfu.cmpt276.turtleescape;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); // Gets key number of key pressed and stores it

        if (code == KeyEvent.VK_W){ // If user press W key
            upPressed = true;
        } if (code == KeyEvent.VK_A){ // If user press A Key
            leftPressed = true;
        } if (code == KeyEvent.VK_S){ // If user press S Key
            downPressed = true;
        } if (code == KeyEvent.VK_D){ // If user press D Key
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W){ // If user press W key
            upPressed = false;
        } if (code == KeyEvent.VK_A){ // If user press A Key
            leftPressed = false;
        } if (code == KeyEvent.VK_S){ // If user press S Key
            downPressed = false;
        } if (code == KeyEvent.VK_D){ // If user press D Key
            rightPressed = false;
        }
    }
}
