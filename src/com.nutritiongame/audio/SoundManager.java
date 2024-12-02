package com.nutritiongame.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, Clip> soundEffects;
    private Clip backgroundMusic;
    private float musicVolume;
    private float effectsVolume;
    private boolean isMuted;

    private SoundManager() {
        soundEffects = new HashMap<>();
        musicVolume = 0.5f;
        effectsVolume = 0.5f;
        isMuted = false;
        loadSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSounds() {
        try {
            // Load background music
            loadMusic("/resources/sounds/background_music.wav");

            // Load sound effects
            loadSoundEffect("card_play", "/resources/sounds/card_play.wav");
            loadSoundEffect("button_click", "/resources/sounds/button_click.wav");
            loadSoundEffect("victory", "/resources/sounds/victory.wav");
            loadSoundEffect("game_start", "/resources/sounds/game_start.wav");
            loadSoundEffect("devil_appear", "/resources/sounds/devil_appear.wav");
            loadSoundEffect("round_start", "/resources/sounds/round_start.wav");
            loadSoundEffect("round_end", "/resources/sounds/round_end.wav");
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }

    private void loadMusic(String path) {
        try {
            File soundFile = new File("src" + path);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioStream);
                setMusicVolume(musicVolume);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.err.println("Music file not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    private void loadSoundEffect(String name, String path) {
        try {
            File soundFile = new File("src" + path);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                soundEffects.put(name, clip);
                setEffectVolume(clip, effectsVolume);
            } else {
                System.err.println("Sound effect file not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error loading sound effect: " + name + " - " + e.getMessage());
        }
    }

    public void playSound(String name) {
        if (isMuted) return;

        Clip clip = soundEffects.get(name);
        if (clip != null) {
            // Stop the clip if it's still playing
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.err.println("Sound effect not found: " + name);
        }
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        if (backgroundMusic != null && backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(Math.max(0.0001f, volume)) / Math.log(10.0) * 20.0);
            gainControl.setValue(Math.max(-80.0f, Math.min(6.0f, dB)));
        }
    }

    public void setEffectsVolume(float volume) {
        this.effectsVolume = volume;
        for (Clip clip : soundEffects.values()) {
            setEffectVolume(clip, volume);
        }
    }

    private void setEffectVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(Math.max(0.0001f, volume)) / Math.log(10.0) * 20.0);
            gainControl.setValue(Math.max(-80.0f, Math.min(6.0f, dB)));
        }
    }

    // Add getters for volume levels
    public float getMusicVolume() {
        return musicVolume;
    }

    public float getEffectsVolume() {
        return effectsVolume;
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            setMusicVolume(0.0f);
            setEffectsVolume(0.0f);
        } else {
            setMusicVolume(musicVolume);
            setEffectsVolume(effectsVolume);
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void startBackgroundMusic() {
        if (backgroundMusic != null && !isMuted) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.start();
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void pauseBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && !isMuted) {
            backgroundMusic.start();
        }
    }

    public void stopAll() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
        for (Clip clip : soundEffects.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }
}