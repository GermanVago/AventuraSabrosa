package com.nutritiongame.tutorial;

import java.awt.Image;
import com.nutritiongame.GameController;

public class TutorialStep {
    private String title;
    private String description;
    private Image image;

    public TutorialStep(String title, String description, String imagePath) {
        this.title = title;
        this.description = description;
        loadImage(imagePath);
    }

    private void loadImage(String path) {
        try {
            this.image = GameController.getInstance().loadImage(path);
        } catch (Exception e) {
            System.err.println("Error loading tutorial image: " + path);
            this.image = null;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }
}