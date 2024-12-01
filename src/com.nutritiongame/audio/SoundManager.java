package com.nutritiongame.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private Map<String, Clip> soundClips;
    private float musicVolume;
    private float effectsVolume;
    private Clip backgroundMusic;
    
    public SoundManager() {
        soundClips = new HashMap<>();
        musicVolume = 1.0f;
        effectsVolume = 1.0f;
        loadSounds();
    }
    
    private void loadSounds() {
        try {
            // Load sound effects
            loadSound("card_play", "/resources/sounds/card_play.wav");
            loadSound("round_start", "/resources/sounds/round_start.wav");
            loadSound("victory", "/resources/sounds/victory.wav");
            loadSound("devil_appear", "/resources/sounds/devil_appear.wav");
            
            // Load and start background music
            loadBackgroundMusic("/resources/sounds/background_music.wav");
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }
    
    private void loadSound(String name, String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(path));
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        soundClips.put(name, clip);
    }
    
    private void loadBackgroundMusic(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(path));
        backgroundMusic = AudioSystem.getClip();
        backgroundMusic.open(audioStream);
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public void playCardSound() {
        playSound("card_play");
    }
    
    public void playRoundStartSound() {
        playSound("round_start");
    }
    
    public void playVictorySound() {
        playSound("victory");
    }
    
    public void playDevilSound() {
        playSound("devil_appear");
    }
    
    private void playSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public void setMusicVolume(float volume) {
        musicVolume = volume;
        if (backgroundMusic != null) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
        }
    }
    
    public void setEffectsVolume(float volume) {
        effectsVolume = volume;
        for (Clip clip : soundClips.values()) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
        }
    }
    
    public void stopAll() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
        for (Clip clip : soundClips.values()) {
            clip.stop();
        }
    }
}