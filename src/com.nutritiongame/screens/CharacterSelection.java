package com.nutritiongame.screens;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import com.nutritiongame.GameController;
import com.nutritiongame.Screen;

public class CharacterSelection extends JPanel implements Screen {
    private final boolean isPvP;
    private final Map<String, Image> characterImages;
    private String selectedCharacter1;
    private String selectedCharacter2;

    public CharacterSelection(boolean isPvP) {
        this.isPvP = isPvP;
        setLayout(new GridLayout(2, 4, 10, 10));
        characterImages = new HashMap<>();

        loadCharacterImages();
        createCharacterButtons();
    }

    private void loadCharacterImages() {
        // Updated paths according to our file structure
        String[] characters = {
                "/images/characters/nina_WH_NL_LT.png",
                "/images/characters/nina_WH_NL.png",
                "/images/characters/nina_WH_SD_LT.png",
                "/images/characters/nina_WH_SD.png",
                "/images/characters/nino_WH_NL_LT1.png",
                "/images/characters/nino_WH_NL.png",
                "/images/characters/nino_WH_SD_1.png",
                "/images/characters/nino_WH_SD_LT1.png"
        };

        for (String character : characters) {
            Image img = GameController.getInstance().loadImage(character);
            characterImages.put(character, img);
        }
    }
    
    private void createCharacterButtons() {
        for (Map.Entry<String, Image> entry : characterImages.entrySet()) {
            JButton characterButton = new JButton(new ImageIcon(entry.getValue()));
            characterButton.addActionListener(e -> selectCharacter(entry.getKey()));
            add(characterButton);
        }
    }
    
    private void selectCharacter(String character) {
        if (selectedCharacter1 == null) {
            selectedCharacter1 = character;
            if (!isPvP) {
                startGame();
            }
        } else if (isPvP && selectedCharacter2 == null) {
            selectedCharacter2 = character;
            startGame();
        }
    }
    
    private void startGame() {
        // Start the game with selected characters
        GameController.getInstance().switchScreen(new GameScreen(selectedCharacter1, selectedCharacter2));
    }
    
    @Override
    public void render() {
        repaint();
    }
    
    @Override
    public void update() {
    }
    
    @Override
    public void handleInput() {
    }
}