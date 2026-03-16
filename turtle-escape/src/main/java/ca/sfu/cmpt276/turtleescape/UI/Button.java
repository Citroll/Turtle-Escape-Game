package ca.sfu.cmpt276.turtleescape.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Represents a clickable button in the UI.
 */
public class Button {
    private int x, y, width, height;
    private String text;
    private Runnable action;

    /**
     * Constructs a Button with the specified position, size, text, and action.
     *
     * @param x      the x-coordinate of the button on the scree
     * @param y      the y-coordinate of the button on the screen
     * @param width  the width of the button
     * @param height the height of the button
     * @param text   the text displayed on the button
     * @param action the action to perform when clicked
     */
    public Button(int x, int y, int width, int height, String text, Runnable action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.action = action;
    }

    /**
     * Checks if the given point is within the button bounds.
     *
     * @param px the x-coordinate of the point
     * @param py the y-coordinate of the point
     * @return if the point is inside the button
     */
    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    /**
     * Draws the button on the given Graphics2D context.
     *
     * @param g2 the Graphics2D context to draw on
     */
    public void draw(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 24));
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textX = x + (width - textWidth) / 2;
        int textY = y + height / 2 + 8;
        g2.drawString(text, textX, textY);
    }

    /**
     * Executes the button's action.
     */
    public void click() {
        if (action != null) {
            action.run();
        }
    }

    // Getters and setters for repositioning
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}