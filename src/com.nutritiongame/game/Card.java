package com.nutritiongame.game;

import com.nutritiongame.GameController;
import java.awt.Image;

public class Card {
    private String name;
    private Image image;
    private Nutrients nutrients;
    private CardType type;
    private String description;
    private int healthScore;

    public enum CardType {
        FOODTRASH,   // Unhealthy foods
        FRUIT,       // High in vitamins
        VEGETABLE,   // Good balance of nutrients
        PROTEIN      // High in proteins (new category)
    }

    public Card(String name, String imagePath, CardType type, int proteins, int vitamins, int carbs, int healthScore, String description) {
        this.name = name;
        this.image = GameController.getInstance().loadImage(imagePath);
        this.type = type;
        this.healthScore = healthScore;
        this.description = description;
        this.nutrients = new Nutrients(proteins, vitamins, carbs);
    }

    public int getNutrientValue(String nutrient) {
        return nutrients.getValue(nutrient);
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public CardType getType() {
        return type;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public String getDescription() {
        return description;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }
}