package ca.sfu.cmpt276.turtleescape.tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;


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

    /**
     * Constructs the TileManager, loads tile images, and loads the map layout.
     *
     * @param gp the GamePanel this tile manager belongs to
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[8];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap();
    }

    /**
     * Loads tile images from the resources folder and assigns them to tile slots.
     * Tile 0 = sand, Tile 1 = water, Tile 2 = castle (barrier).
     */
    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/sand.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/water.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/castle.png"));
            tile[2].collision = true; // add for all barrier tiles

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/tree.png"));
            tile[3].collision = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/kid.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/plastic_bag.png"));
            tile[5].collision = true;

        } catch (IOException e) {
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

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
