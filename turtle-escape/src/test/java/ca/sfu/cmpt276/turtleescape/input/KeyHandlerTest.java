package ca.sfu.cmpt276.turtleescape.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.awt.Component;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for KeyHandler.
 * Verifies that key press and release events correctly update direction flags
 * and the escape flag. Uses synthetic KeyEvent objects rather than real input.
 */
public class KeyHandlerTest {

    KeyHandler keyH;

    // A dummy component is needed to construct KeyEvent objects
    Component dummyComponent = new Component() {};

    @BeforeEach
    public void setUp() {
        keyH = new KeyHandler();
    }

    /** Helper: simulate pressing a key */
    private void press(int keyCode) {
        keyH.keyPressed(new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, keyCode, (char) keyCode));
    }

    /** Helper: simulate releasing a key */
    private void release(int keyCode) {
        keyH.keyReleased(new KeyEvent(dummyComponent, KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, keyCode, (char) keyCode));
    }

    /**
     * Pressing W should set upPressed to true.
     */
    @Test
    public void testWKeyPressedSetsUpPressed() {
        press(KeyEvent.VK_W);
        assertTrue(keyH.upPressed, "upPressed should be true after pressing W");
    }

    /**
     * Releasing W should clear upPressed.
     */
    @Test
    public void testWKeyReleasedClearsUpPressed() {
        press(KeyEvent.VK_W);
        release(KeyEvent.VK_W);
        assertFalse(keyH.upPressed, "upPressed should be false after releasing W");
    }

    /**
     * Pressing S should set downPressed to true.
     */
    @Test
    public void testSKeyPressedSetsDownPressed() {
        press(KeyEvent.VK_S);
        assertTrue(keyH.downPressed);
    }

    /**
     * Releasing S should clear downPressed.
     */
    @Test
    public void testSKeyReleasedClearsDownPressed() {
        press(KeyEvent.VK_S);
        release(KeyEvent.VK_S);
        assertFalse(keyH.downPressed);
    }

    /**
     * Pressing A should set leftPressed to true.
     */
    @Test
    public void testAKeyPressedSetsLeftPressed() {
        press(KeyEvent.VK_A);
        assertTrue(keyH.leftPressed);
    }

    /**
     * Releasing A should clear leftPressed.
     */
    @Test
    public void testAKeyReleasedClearsLeftPressed() {
        press(KeyEvent.VK_A);
        release(KeyEvent.VK_A);
        assertFalse(keyH.leftPressed);
    }

    /**
     * Pressing D should set rightPressed to true.
     */
    @Test
    public void testDKeyPressedSetsRightPressed() {
        press(KeyEvent.VK_D);
        assertTrue(keyH.rightPressed);
    }

    /**
     * Releasing D should clear rightPressed.
     */
    @Test
    public void testDKeyReleasedClearsRightPressed() {
        press(KeyEvent.VK_D);
        release(KeyEvent.VK_D);
        assertFalse(keyH.rightPressed);
    }

    /**
     * Pressing ESCAPE should set escapePressed to true.
     */
    @Test
    public void testEscapeKeyPressedSetsEscapePressed() {
        press(KeyEvent.VK_ESCAPE);
        assertTrue(keyH.escapePressed);
    }

    /**
     * Releasing ESCAPE should clear escapePressed.
     */
    @Test
    public void testEscapeKeyReleasedClearsEscapePressed() {
        press(KeyEvent.VK_ESCAPE);
        release(KeyEvent.VK_ESCAPE);
        assertFalse(keyH.escapePressed);
    }

    /**
     * All flags should be false by default before any key events.
     */
    @Test
    public void testAllFlagsDefaultToFalse() {
        assertFalse(keyH.upPressed);
        assertFalse(keyH.downPressed);
        assertFalse(keyH.leftPressed);
        assertFalse(keyH.rightPressed);
        assertFalse(keyH.escapePressed);
    }

    /**
     * Multiple keys pressed simultaneously should each set their flag independently.
     */
    @Test
    public void testMultipleKeysPressedSimultaneously() {
        press(KeyEvent.VK_W);
        press(KeyEvent.VK_D);
        assertTrue(keyH.upPressed,    "W should set upPressed");
        assertTrue(keyH.rightPressed, "D should set rightPressed");
        assertFalse(keyH.downPressed);
        assertFalse(keyH.leftPressed);
    }

    /**
     * Releasing one key should not clear other keys that are still held.
     */
    @Test
    public void testReleasingOneKeyDoesNotAffectOthers() {
        press(KeyEvent.VK_W);
        press(KeyEvent.VK_D);
        release(KeyEvent.VK_W);
        assertFalse(keyH.upPressed,   "W released so upPressed should be false");
        assertTrue(keyH.rightPressed, "D still held so rightPressed should stay true");
    }
}
