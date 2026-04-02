package ca.sfu.cmpt276.turtleescape.audio;

/**
 * Manages all audio playback for the game.
 * Extracted from GamePanel to separate audio concerns (Smell 5 + 6 fix).
 */
public class AudioManager {

    private final Sound music  = new Sound();
    private final Sound se     = new Sound();
    private final Sound moveSE = new Sound();

    public void playMusic(int track) {
        music.setFile(track);
        music.play();
        music.loop();
        music.setVolume(0.72f);
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int index) {
        se.setFile(index);
        se.play();
    }

    public void playSE(int index, float volume) {
        se.setFile(index);
        se.setVolume(volume);
        se.play();
    }

    public void playMoveSE(int index) {
        moveSE.setFile(index);
        moveSE.play();
        moveSE.loop();
    }

    public void stopMoveSE() {
        moveSE.stop();
    }

    public void stopAll() {
        music.stop();
        moveSE.stop();
        se.stop();
    }
}