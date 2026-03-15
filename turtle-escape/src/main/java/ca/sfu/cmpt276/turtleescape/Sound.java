package ca.sfu.cmpt276.turtleescape;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * handles loading and playing audio files for music and sound effects.
 * uses javax.sound.sampled.Clip for .wav playback.
 */
public class Sound {

    /** the audio clip used for playback */
    Clip clip;

    /** array of urls pointing to .wav sound files in resources */
    URL[] soundURL = new URL[30];

    /**
     * constructs a sound instance and loads all sound file paths
     * into the url array.
     */
    public Sound() {
        soundURL[0] = getClass().getClassLoader().getResource("sound/music.wav");
        soundURL[1] = getClass().getClassLoader().getResource("sound/pickup.wav");
        soundURL[2] = getClass().getClassLoader().getResource("sound/walk.wav");
        soundURL[3] = getClass().getClassLoader().getResource("sound/swim.wav");
        soundURL[4] = getClass().getClassLoader().getResource("sound/death.wav");
        soundURL[5] = getClass().getClassLoader().getResource("sound/hurt.wav");
    }

    /**
     * loads the .wav file at the given index into the clip for playback.
     *
     * @param i the index of the sound file in the url array
     */
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * starts playback of the currently loaded clip.
     */
    public void play() {
        clip.start();
    }

    /**
     * loops the currently loaded clip continuously.
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * stops the currently playing clip, if one exists.
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    /**
     * sets the playback volume of the currently loaded clip.
     *
     * @param volume a float from 0.0 (silent) to 1.0 (full volume)
     */
    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = fc.getMinimum();
            float max = fc.getMaximum();
            float gain = min + (max - min) * volume;
            fc.setValue(gain);
        }
    }
}
