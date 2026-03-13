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
 * Manages all tiles in the game world.
 * Responsible for loading tile images, reading the map layout from a text file,
 * and rendering the tile map to the screen each frame.
 */
public class TileManager {

    /** Reference to the game panel for accessing screen and tile dimensions */
    GamePanel gp;

    /** Array of all available tile types indexed by tile number */
    public Tile[] tile;

    /** 2D array storing the tile number for each cell in the map grid */
    public int[][] mapTileNum;

    /** Frame counter for water animations */
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

        getTileImage();
        loadMap();
    }

    /**
     * Updates water animation tile every 30 ticks
     */
    public void update(){
        animationCounter++;
        if(animationCounter >= 30){
            animationFrame++;
            if(animationFrame > 1){
                animationFrame = 0;
            }
            animationCounter = 0;   
        }
    }

    /**
     * Loads tile images from the resources folder and assigns them to tile slots.
     * Tile 0 = sand, Tile 1 = water, Tile 2 = castle (barrier).
     */
    public void getTileImage() {
        //SAND 0-1 SAND, SAND+WATER EDGE
        setUp(0, "tiles/sand", false);

        //WATER 2-5 WATER, WATER_2, WATER+DEEP_WATER, DEEP_WATER_2
        setUp(2, "tiles/water1", false);
        setUp(3, "tiles/water2", false);
        //SANDCASTLE 6
        setUp(6, "tiles/castle", true);
        //TREE 7
        setUp(7, "tiles/tree", true);
        //KID 8
        setUp(8, "enemies/kid", true);
    }


    /**
     * Loads a single tile image from resources, scales it to the tile size,
     * and assigns it to the given index in the tile array.
     *
     * @param index     the index in the tile array to assign this tile to
     * @param imagePath the resource path to the tile image (without file extension)
     * @param collision whether this tile should block entity movement
     */
    public void setUp(int index, String imagePath, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads the map layout from a text file and populates the mapTileNum grid.
     * Each number in the file corresponds to a tile type index.
     * The map file should be located at resources/maps
     */
    public void loadMap(){
        try {
            InputStream is = getClass().getResourceAsStream("/maps/world01.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine(); // read 1 line of text file

                while(col < gp.maxWorldCol) {
                    String[] numbers = line.split(" "); // split each number into array of Strings

                    int num = Integer.parseInt(numbers[col]); // convert string numbers to integers

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }

            br.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Draws the entire tile map to the screen.
     * Iterates over every cell in the grid and renders the appropriate tile image.
     *
     * @param g2 the Graphics2D context used for rendering
     */
    public void draw(Graphics2D g2){

        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow];

            if(tileNum == 2 && animationFrame == 1) tileNum = 3;

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (tileNum < tile.length && tile[tileNum] != null && tile[tileNum].image != null) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
