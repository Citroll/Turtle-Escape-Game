package ca.sfu.cmpt276.turtleescape.UI;

public interface PanelSettings {

    /**
     * The base tile size in pixels before scaling
     */
    final int originalTileSize = 16;

    /**
     * Scale factor applied to the original tile size
     */
    final int scale = 3;

    /**
     * The actual tile size after scaling (48x48 pixels)
     */
    public final int tileSize = 48;

    /**
     * Number of tile columns on screen
     */
    public final int maxScreenCol = 16;

    /**
     * Number of tile rows on screen
     */
    public final int maxScreenRow = 12;

    /**
     * Total screen width in pixels
     */
    public final int screenWidth = 768;

    /**
     * Total screen height in pixels
     */
    public final int screenHeight = 576;

    public final int maxWorldCol = 100;
    public final int maxWorldRow = 22;

    /**
     * Target frames per second for the game loop
     */
    public final int FPS = 60;
}
