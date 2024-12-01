package com.nutritiongame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.net.URL;
import com.nutritiongame.screens.LoadingScreen;

public class GameController {
    private JFrame mainWindow;
    private Screen currentScreen;
    private static GameController instance;
    private Map<String, Image> imageCache;

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

    public Image loadImage(String path) {
        if (!imageCache.containsKey(path)) {
            try {
                // Try to load from resources
                URL resourceUrl = getClass().getResource(path);
                if (resourceUrl == null) {
                    // If not found in resources, try as file path
                    File file = new File("resources" + path);
                    if (file.exists()) {
                        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                        imageCache.put(path, icon.getImage());
                    } else {
                        System.err.println("Cannot find image: " + path);
                        // Return a placeholder image or null
                        return createPlaceholderImage();
                    }
                } else {
                    ImageIcon icon = new ImageIcon(resourceUrl);
                    imageCache.put(path, icon.getImage());
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + path);
                e.printStackTrace();
                return createPlaceholderImage();
            }
        }
        return imageCache.get(path);
    }

    private Image createPlaceholderImage() {
        // Create a simple colored rectangle as placeholder
        BufferedImage placeholder = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();
        return placeholder;
    }

    public void switchScreen(Screen screen) {
        currentScreen = screen;
        mainWindow.getContentPane().removeAll();
        if (screen instanceof JPanel) {
            mainWindow.add((JPanel)screen);
        }
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    public void start() {
        mainWindow.setVisible(true);
        switchScreen(new LoadingScreen());
    }
}