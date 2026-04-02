package ca.sfu.cmpt276.turtleescape.audio;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {

    // Smell 8 fix: named constants so call sites like playSE(4) become playSE(SFX_DEATH)
    public static final int MUSIC       = 0;
    public static final int SFX_PICKUP  = 1;
    public static final int SFX_WALK    = 2;
    public static final int SFX_SWIM    = 3;
    public static final int SFX_DEATH   = 4;
    public static final int SFX_HURT    = 5;

    // Smell 8 fix: array sized to actual number of sounds, not 30
    private static final int SOUND_COUNT = 6;

    Clip clip;
    URL[] soundURL = new URL[SOUND_COUNT];

    public Sound() {
        soundURL[MUSIC]      = getClass().getClassLoader().getResource("sound/music.wav");
        soundURL[SFX_PICKUP] = getClass().getClassLoader().getResource("sound/pickup.wav");
        soundURL[SFX_WALK]   = getClass().getClassLoader().getResource("sound/walk.wav");
        soundURL[SFX_SWIM]   = getClass().getClassLoader().getResource("sound/swim.wav");
        soundURL[SFX_DEATH]  = getClass().getClassLoader().getResource("sound/death.wav");
        soundURL[SFX_HURT]   = getClass().getClassLoader().getResource("sound/hurt.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        // Smell 8 fix: null guard matching stop() — was missing in original
        if (clip != null) clip.start();
    }

    public void loop() {
        // Smell 8 fix: null guard matching stop() — was missing in original
        if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null) clip.stop();
    }

    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = fc.getMinimum();
            float max = fc.getMaximum();
            fc.setValue(min + (max - min) * volume);
        }
    }
}