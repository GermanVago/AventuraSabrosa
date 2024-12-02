package com.nutritiongame.game;

import com.nutritiongame.GameController;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

public class Card {
    private String name;
    private Image image;
    private Map<String, Integer> nutrients;
    private CardType type;
    private String description;
    private int healthScore; // 0-100 indicating how healthy the food is

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

        nutrients = new HashMap<>();
        nutrients.put("proteins", proteins);
        nutrients.put("vitamins", vitamins);
        nutrients.put("carbs", carbs);
    }

    public int getNutrientValue(String nutrient) {
        return nutrients.getOrDefault(nutrient, 0);
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
}