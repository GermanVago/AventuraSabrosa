package com.nutritiongame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    private JFrame mainWindow;
    private Screen currentScreen;
    private static GameController instance;
    private final Map<String, Image> imageCache;

    private GameController() {
        mainWindow = new JFrame("Juego de Nutrición");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(1024, 768);
        mainWindow.setLocationRelativeTo(null);
        imageCache = new HashMap<>();
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void switchScreen(Screen screen) {
        currentScreen = screen;
        mainWindow.getContentPane().removeAll();
        if (screen instanceof JPanel) {
            mainWindow.add((JPanel) screen);
        }
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    public Image loadImage(String path) {
        if (!imageCache.containsKey(path)) {
            imageCache.put(path, new ImageIcon(path).getImage());
        }
        return imageCache.get(path);
    }

    public void start() {
        mainWindow.setVisible(true);
        switchScreen(new com.nutritiongame.screens.LoadingScreen());
    }
}
