package ca.sfu.cmpt276.turtleescape.integration;

import ca.sfu.cmpt276.turtleescape.audio.AudioManager;
import ca.sfu.cmpt276.turtleescape.object.IceCreamManager;
import ca.sfu.cmpt276.turtleescape.audio.Sound;
import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.ai.Pathfinder;
import ca.sfu.cmpt276.turtleescape.collision.CollisionChecker;
import ca.sfu.cmpt276.turtleescape.entity.Entity;
import ca.sfu.cmpt276.turtleescape.entity.Player;
import ca.sfu.cmpt276.turtleescape.input.KeyHandler;
import ca.sfu.cmpt276.turtleescape.object.SuperObject;
import ca.sfu.cmpt276.turtleescape.tile.Tile;
import ca.sfu.cmpt276.turtleescape.UI.UI;
import ca.sfu.cmpt276.turtleescape.tile.TileManager;

import java.awt.*;
import java.lang.reflect.Field;

public class FakeGamePanel {

    /**
     * Sound subclass that no-ops every method.
     * Prevents playSE/playMusic/stopMoveSE from touching javax.sound at all.
     */

    public static final int COLS = 100;
    public static final int ROWS = 22;

    public static GamePanel build() {
        try {
            GamePanel gp = (GamePanel) allocate(GamePanel.class);

            setInt(gp, GamePanel.class, "tileSize",     48);
            // maxWorldCol=100 and maxWorldRow=22 are compile-time constants in GamePanel
            // — reflection cannot override them. Arrays must match exactly.

            setInt(gp, GamePanel.class, "screenWidth",  768);
            setInt(gp, GamePanel.class, "screenHeight", 576);
            setInt(gp, GamePanel.class, "maxScreenCol", 16);
            setInt(gp, GamePanel.class, "maxScreenRow", 12);

            set(gp, GamePanel.class, "tileM",   buildTileManager(gp));
            set(gp, GamePanel.class, "player",  buildPlayer(gp));
            set(gp, GamePanel.class, "cChecker", new CollisionChecker(gp));
            set(gp, GamePanel.class, "pFinder",  new Pathfinder(gp));
            set(gp, GamePanel.class, "obj",      new SuperObject[50]);
            set(gp, GamePanel.class, "enemy",    new Entity[20]);

            set(gp, GamePanel.class, "audio", new AudioManager() {
                @Override public void playMusic(int i) {}
                @Override public void stopMusic() {}
                @Override public void playSE(int i) {}
                @Override public void playSE(int i, float v) {}
                @Override public void playMoveSE(int i) {}
                @Override public void stopMoveSE() {}
                @Override public void stopAll() {}
            });

            // Stub UI so triggerGreenFlash() doesn't crash
            UI ui = (UI) allocate(UI.class);
            set(ui, UI.class, "gp", gp);   // if UI uses gp internally
            set(gp, GamePanel.class, "ui", ui);

            IceCreamManager iceCreamManager = (IceCreamManager) allocate(IceCreamManager.class);
            set(gp, GamePanel.class, "iceCreamManager", iceCreamManager);

            set(gp, GamePanel.class, "gameState", GamePanel.GameState.PLAYING);

            return gp;
        } catch (Exception e) {
            throw new RuntimeException("FakeGamePanel.build failed: " + e.getMessage(), e);
        }
    }

    private static TileManager buildTileManager(GamePanel gp) throws Exception {
        int cols = COLS;
        int rows = ROWS;
        TileManager tm = (TileManager) allocate(TileManager.class);
        set(tm, TileManager.class, "gp", gp);

        Tile[] tiles = new Tile[50];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile();
            tiles[i].collision = false;
        }
        tiles[1].collision = true; // tile type 1 = solid wall in tests

        set(tm, TileManager.class, "tile", tiles);

        int[][] mapTileNum    = new int[cols][rows];
        int[][] mapOverlayNum = new int[cols][rows];
        for (int c = 0; c < cols; c++)
            for (int r = 0; r < rows; r++)
                mapOverlayNum[c][r] = -1;

        set(tm, TileManager.class, "mapTileNum",    mapTileNum);
        set(tm, TileManager.class, "mapOverlayNum", mapOverlayNum);
        return tm;
    }

    private static Player buildPlayer(GamePanel gp) throws Exception {
        Player player = (Player) allocate(Player.class);
        set(player, Entity.class, "gp",   gp);
        set(player, Player.class, "keyH", new KeyHandler());
        setInt(player, Player.class, "screenX", 384);
        setInt(player, Player.class, "screenY", 288);

        player.worldX            = 48 * 3;
        player.worldY            = 48 * 11;
        player.speed             = 4;
        player.direction         = "down";
        player.solidArea         = new Rectangle(4, 4, 40, 40);
        player.solidAreaDefaultX = 4;
        player.solidAreaDefaultY = 4;
        player.type              = 0;
        player.score             = 0;
        return player;
    }

    private static Object allocate(Class<?> clazz) throws Exception {
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field f = unsafeClass.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Object unsafe = f.get(null);
        return unsafeClass.getMethod("allocateInstance", Class.class).invoke(unsafe, clazz);
    }

    public static void set(Object target, Class<?> declaredIn, String fieldName, Object value) throws Exception {
        Field f = declaredIn.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    public static void setInt(Object target, Class<?> declaredIn, String fieldName, int value) throws Exception {
        Field f = declaredIn.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    public static void setSolidTile(GamePanel gp, int col, int row) {
        gp.tileM.mapTileNum[col][row] = 1;
    }

    public static void setWalkableTile(GamePanel gp, int col, int row) {
        gp.tileM.mapTileNum[col][row] = 0;
    }
}