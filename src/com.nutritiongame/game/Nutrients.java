package com.nutritiongame.game;

public class Nutrients {
    private int proteins;
    private int vitamins;
    private int carbs;

    public Nutrients(int proteins, int vitamins, int carbs) {
        this.proteins = proteins;
        this.vitamins = vitamins;
        this.carbs = carbs;
    }

    public int getValue(String nutrientType) {
        switch (nutrientType) {
            case "proteins":
                return proteins;
            case "vitamins":
                return vitamins;
            case "carbs":
                return carbs;
            default:
                return 0;
        }
    }

    public int getProteins() {
        return proteins;
    }

    public int getVitamins() {
        return vitamins;
    }

    public int getCarbs() {
        return carbs;
    }
}