package ca.sfu.cmpt276.turtleescape.tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.UtilityTool;

/**
 * Manages all tiles in the game world. Responsible for loading tile images,
 * reading the map layout from a text file, and rendering the tile map to the
 * screen each frame. Supports a two-layer system: a base layer (world01.txt)
 * and an optional overlay layer (world01_overlay.txt) for transparent tiles
 * like buoys.
 */
public class TileManager {

    /**
     * Reference to the game panel for accessing screen and tile dimensions
     */
    GamePanel gp;

    /**
     * Array of all available tile types indexed by tile number
     */
    public Tile[] tile;

    /**
     * 2D array storing the tile number for each cell in the map grid (base
     * layer)
     */
    public int[][] mapTileNum;

    /**
     * 2D array storing overlay tile numbers for each cell (-1 = no overlay).
     * Overlay tiles are drawn on top of the base layer, so their PNG must have
     * a transparent background. Collision is still determined by
     * Tile.collision.
     */
    public int[][] mapOverlayNum;

    /**
     * Frame counter for water animations
     */
    private int animationCounter = 0;
    private int animationFrame = 0;

    /**
     * Constructs the TileManager, loads tile images, and loads the map layout.
     *
     * @param gp the GamePanel this tile manager belongs to
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[50];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        mapOverlayNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        // Fill overlay grid with -1 (empty) by default
        for (int c = 0; c < gp.maxWorldCol; c++) {
            for (int r = 0; r < gp.maxWorldRow; r++) {
                mapOverlayNum[c][r] = -1;
            }
        }

        getTileImage();
        loadMap("/maps/world01.txt", mapTileNum);
        loadMap("/maps/world01_overlay.txt", mapOverlayNum);
    }

    /**
     * Updates water animation tile every 30 ticks
     */
    public void update() {
        animationCounter++;
        if (animationCounter >= 30) {
            animationFrame++;
            if (animationFrame > 1) {
                animationFrame = 0;
            }
            animationCounter = 0;
        }
    }

    /**
     * Loads tile images from the resources folder and assigns them to tile
     * slots.
     */
    public void getTileImage() {
        //BACKGROUND MAP
        setUp(0, "tiles/sand", false); //0, sand
        setUp(1, "tiles/sandwater1", false); //1, sandwater1
        setUp(2, "tiles/sandwater2", false); //2, sandwater2
        setUp(3, "tiles/sandwaterflipped1", false); //3, sandwaterflipped1
        setUp(4, "tiles/sandwaterflipped2", false); //4, sandwaterflipped2
        setUp(5, "tiles/water1", false); //5, water1
        setUp(6, "tiles/water2", false); //6, water2
        setUp(7, "tiles/deepwater1", false); //7, deepwater1
        setUp(8, "tiles/deepwater2", false);//8, deepwater2
        setUp(9, "tiles/deepwateredge1", false); //9, deepwateredge1
        setUp(10, "tiles/deepwateredge2", false); //10, deepwateredge2
        setUp(11, "tiles/deepwateredgeflipped1", false); //11, deepwateredgeflipped1
        setUp(12, "tiles/deepwateredgeflipped2", false); //12, deepwateredgeflipped2
        setUp(13, "tiles/sandflipped", false); //sand, flipped

        //BARRIERS
        setUp(20, "tiles/tree", true); //20, tree
        setUp(21, "tiles/castle", true); //21, sandcastle
        setUp(22, "tiles/buoy", true); //22, buoy
        setUp(23, "tiles/rocks", true); //23 rock
        setUp(24, "tiles/coral", true); //24, coral
        setUp(25, "tiles/egg", false); //25, egg
        setUp(26, "objects/brokenegg", false); //26, broken egg
        //25, sunkenboat
    }

    /**
     * Loads a single tile image from resources, scales it to the tile size, and
     * assigns it to the given index in the tile array.
     *
     * @param index the index in the tile array to assign this tile to
     * @param imagePath the resource path to the tile image (without file
     * extension)
     * @param collision whether this tile should block entity movement
     */
    public void setUp(int index, String imagePath, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a map layout from the given resource file and populates the target array.
     * Uses whitespace-tolerant parsing so map files can use any spacing.
     *
     * @param mapFile the resource path to the map text file
     * @param targetArray the 2D array to populate with tile numbers
     */
    public void loadMap(String mapFile, int[][] targetArray) {
        try {
            InputStream is = getClass().getResourceAsStream(mapFile);
            if (is == null) return; // overlay file is optional
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String[] numbers = br.readLine().trim().split("\\s+");
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    targetArray[col][row] = Integer.parseInt(numbers[col]);
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resolves animated tile variants for the current animation frame.
     */
    private int resolveAnimation(int tileNum) {
        if (animationFrame == 1) {
            if (tileNum == 1) {
                return 2;  // sandwater

            }
            if (tileNum == 3) {
                return 4;  // sandwaterflipped

            }
            if (tileNum == 5) {
                return 6;  // water

            }
            if (tileNum == 7) {
                return 8;  // deepwater

            }
            if (tileNum == 9) {
                return 10; // deepwateredge

            }
            if (tileNum == 11) {
                return 12; // deepwateredgeflipped

            }
        }
        return tileNum;
    }

    /**
     * Draws the tile map in two passes: Pass 1 — base layer (world01.txt) Pass
     * 2 — overlay layer (world01_overlay.txt), transparent PNGs drawn on top.
     *
     * @param g2 the Graphics2D context used for rendering
     */
    public void draw(Graphics2D g2) {

        // --- Pass 1: base layer ---
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = resolveAnimation(mapTileNum[worldCol][worldRow]);

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (tileNum < tile.length && tile[tileNum] != null && tile[tileNum].image != null) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        // --- Pass 2: overlay layer ---
        worldCol = 0;
        worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int overlayNum = mapOverlayNum[worldCol][worldRow];

            if (overlayNum != -1) {
                int tileNum = resolveAnimation(overlayNum);

                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                if (tileNum < tile.length && tile[tileNum] != null && tile[tileNum].image != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, null);
                }
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
