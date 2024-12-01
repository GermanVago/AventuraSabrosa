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
    
    public enum CardType {
        FOODTRASH,
        FRUIT,
        VEGETABLE
    }
    
    public Card(String name, String imagePath, CardType type, int proteins, int vitamins, int carbs) {
        this.name = name;
        this.image = GameController.getInstance().loadImage(imagePath);
        this.type = type;
        
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
}