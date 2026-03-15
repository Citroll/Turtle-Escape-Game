package ca.sfu.cmpt276.turtleescape.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.text.DecimalFormat;

public class UI {

    GamePanel gp; // Game panel
    Font font;
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#00.00");

    /** The play button for the title screen */
    private Button playButton;

    /** The resume button for the pause screen */
    private Button resumeButton;

    /** The restart button for the death screen*/
    private Button restartButton;

    /** Damage flash */
    private int flashTimer = 0;
    private final int flashDuration = 20;

    /** Score collected green flash*/
    private int greenFlashTimer = 0;
    private final int greenFlashDuration = 20;

    /** The continue/back to title button on the win screen */
    private Button winButton;

    /**
     * Represents the UI for the game
     * Handles the display settings for the score and timer
     * 
     * @param gp the GamePanel this player belongs to
     */
    public UI(GamePanel gp) {
        this.gp = gp;

        font = new Font("Arial", Font.PLAIN, 50);
        playButton = new Button(0, 0, 200, 50, "PLAY", () -> gp.gameState = GamePanel.GameState.PLAYING);
        resumeButton = new Button(0, 0, 200, 50, "RESUME", () -> gp.gameState = GamePanel.GameState.PLAYING);
        restartButton = new Button(0, 0, 200, 50, "RESTART", () -> gp.restartGame());
        winButton = new Button(0, 0, 200, 50, "PLAY AGAIN", () -> gp.restartGame());
    }

    /**
     * Displays the UI including the time and text for the player's score.
     * Only increments the timer when the game is actively playing.
     *
     * @param g2     the Graphics2D context used for rendering
     * @param state  the current game state
     * @param width  the current width of the panel
     * @param height the current height of the panel
     */
    public void draw(Graphics2D g2, GamePanel.GameState state, int width, int height) {
        FontRenderContext frc = g2.getFontRenderContext();
        g2.setFont(font);

        /**
         * Time settings
         */
        if (state == GamePanel.GameState.PLAYING) {
            playTime += (double) 1 / 60;
        }
        GlyphVector timeGV = font.createGlyphVector(frc, "Time: " + dFormat.format(playTime));
        Shape timeShape = timeGV.getOutline(20, 40);
        g2.setColor(Color.white);
        g2.fill(timeShape);
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.draw(timeShape);

        /**
         * Text settings
         */
        GlyphVector gv = font.createGlyphVector(frc, "Score: " + gp.player.score);
        int scoreWidth = gv.getVisualBounds().getBounds().width;
        int scoreX = Math.max(20, width - scoreWidth - 20); // Right-aligned with padding
        Shape textShape = gv.getOutline(scoreX, 40);
        g2.setColor(Color.white);
        g2.fill(textShape);
        // **Outline settings */
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.draw(textShape);
    }

    /**
     * Draws the title screen with game title and play button.
     *
     * @param g2     the Graphics2D context used for rendering
     * @param width  the current width of the panel
     * @param height the current height of the panel
     */
    public void drawTitleScreen(Graphics2D g2, int width, int height) {
        // Set background to black
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);

        // Set font for title
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.setColor(Color.WHITE);

        // Draw title
        String title = "Turtle Escape";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        int titleX = (width - titleWidth) / 2;
        int titleY = height / 2 - 50;
        g2.drawString(title, titleX, titleY);

        // Position and draw play button
        int buttonX = (width - playButton.getWidth()) / 2;
        int buttonY = height / 2 + 50;
        playButton.setX(buttonX);
        playButton.setY(buttonY);
        playButton.draw(g2);
    }

    /**
     * Draws the pause screen with semi-transparent overlay, "PAUSED" text, and
     * resume button.
     *
     * @param g2     the Graphics2D context used for rendering
     * @param width  the current width of the panel
     * @param height the current height of the panel
     */
    public void drawPauseScreen(Graphics2D g2, int width, int height) {
        // Draw semi-transparent black overlay
        g2.setColor(new Color(0, 0, 0, 150)); // 150/255 alpha for semi-transparency
        g2.fillRect(0, 0, width, height);

        // Draw "PAUSED" text
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.setColor(Color.WHITE);
        String pausedText = "PAUSED";
        int textWidth = g2.getFontMetrics().stringWidth(pausedText);
        int textX = (width - textWidth) / 2;
        int textY = height / 2 - 50;
        g2.drawString(pausedText, textX, textY);

        // Position and draw resume button
        int buttonX = (width - resumeButton.getWidth()) / 2;
        int buttonY = height / 2 + 50;
        resumeButton.setX(buttonX);
        resumeButton.setY(buttonY);
        resumeButton.draw(g2);
    }



    /**
     * Draws the death screen with a semi-transparent black overlay, a "YOU DIED"
     * title, and a restart button.
     *
     * @param g2     the Graphics2D context used for rendering
     * @param width  the current width of the panel
     * @param height the current height of the panel
     */
    public void drawDeathScreen(Graphics2D g2, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, width, height);

        g2.setFont(new Font("Arial", Font.BOLD, 64));
        g2.setColor(Color.RED);
        String text = "YOU DIED";
        int textWidth = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (width - textWidth) / 2, height / 2 - 50);

        int buttonX = (width - restartButton.getWidth()) / 2;
        int buttonY = height / 2 + 20;
        restartButton.setX(buttonX);
        restartButton.setY(buttonY);
        restartButton.draw(g2);
    }


    public void drawWinScreen(Graphics2D g2, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, width, height);

        g2.setFont(new Font("Arial", Font.BOLD, 64));
        g2.setColor(Color.GREEN);
        String title = "YOU ESCAPED!";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (width - titleWidth) / 2, height / 2 - 80);

        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.setColor(Color.WHITE);
        String scoreText = "Score: " + gp.player.score;
        int scoreWidth = g2.getFontMetrics().stringWidth(scoreText);
        g2.drawString(scoreText, (width - scoreWidth) / 2, height / 2 - 20);

        String timeText = "Time: " + dFormat.format(playTime);
        int timeWidth = g2.getFontMetrics().stringWidth(timeText);
        g2.drawString(timeText, (width - timeWidth) / 2, height / 2 + 30);

        int buttonX = (width - winButton.getWidth()) / 2;
        int buttonY = height / 2 + 80;
        winButton.setX(buttonX);
        winButton.setY(buttonY);
        winButton.draw(g2);
    }


    /**
     * Overlays a red screen that slowly fades away when the player interacts
     * with a punishment.
     *
     * @param g2     the Graphics2D context used for rendering
     * @param width  the current width of the panel
     * @param height the current height of the panel
     */
    public void drawRedFlash(Graphics2D g2, int width, int height){
        if(flashTimer > 0){
            float fade = (float) flashTimer / flashDuration;
            g2.setColor(new Color(255, 0, 0, (int)(120 * fade)));
            g2.fillRect(0, 0, width, height);
            flashTimer--;
        }
    }

    /**
     * Overlays a green screen that slowly fades away when the player interacts
     * with a punishment.
     *
     * @param g2     the Graphics2D context used for rendering
     * @param width  the current width of the panel
     * @param height the current height of the panel
     */
    public void drawGreenFlash(Graphics2D g2, int width, int height) {
        if (greenFlashTimer > 0) {
            float fade = (float) greenFlashTimer / greenFlashDuration;
            g2.setColor(new Color(0, 255, 0, (int)(120 * fade)));
            g2.fillRect(0, 0, width, height);
            greenFlashTimer--;
        }
    }

    /**
     * Handles mouse clicks for UI elements based on the current game state.
     *
     * @param e     the MouseEvent containing click coordinates
     * @param state the current game state
     */
    public void handleMouseClick(MouseEvent e, GamePanel.GameState state) {
        if (state == GamePanel.GameState.TITLE) {
            if (playButton.contains(e.getX(), e.getY())) {
                playButton.click();
            }
        } else if (state == GamePanel.GameState.PAUSED) {
            if (resumeButton.contains(e.getX(), e.getY())) {
                resumeButton.click();
            }
        } else if (state == GamePanel.GameState.DEAD) {
            if (restartButton.contains(e.getX(), e.getY())) {
                restartButton.click();
            }
        } else if (state == GamePanel.GameState.WIN) {
            if (winButton.contains(e.getX(), e.getY())) {
                winButton.click();
            }
        }
    }

    // redFlash helper
    public void triggerFlash() {
        flashTimer = flashDuration;
    }

    // GreenFlash helper
    public void triggerGreenFlash() {
        greenFlashTimer = greenFlashDuration;
    }
}
