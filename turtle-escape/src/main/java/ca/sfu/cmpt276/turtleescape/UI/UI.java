package ca.sfu.cmpt276.turtleescape.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.text.DecimalFormat;

public class UI {

    GamePanel gp; //Game panel
    Font font;
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#00.00");

    /**
     * Represents the UI for the game
     * Handles the display settings for the score and timer
     * @param gp the GamePanel this player belongs to
     */
    public UI(GamePanel gp) {
        this.gp = gp;

        font = new Font("Arial", Font.PLAIN, 50);
    }

    /**
     * Displays the UI including the time and text for the player's score
     */
    public void draw(Graphics2D g2) {
        FontRenderContext frc = g2.getFontRenderContext();
        g2.setFont(font);

        /**
         * Time settings
         */
        playTime += (double) 1 / 60;
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
        Shape textShape = gv.getOutline(gp.tileSize*10, 40);
        g2.setColor(Color.white);
        g2.fill(textShape);
        //**Outline settings */
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.draw(textShape);
    }

}
