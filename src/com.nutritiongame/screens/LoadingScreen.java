package com.nutritiongame.screens;

import javax.swing.*;
import java.awt.*;
import com.nutritiongame.GameController;
import com.nutritiongame.Screen;

public class LoadingScreen extends JPanel implements Screen {
    private Image backgroundImage;
    private JButton startButton;

    public LoadingScreen() {
        setLayout(null);
        // Updated path according to our file structure
        backgroundImage = GameController.getInstance().loadImage("Aventura Sabrosa/src/resources/images/backgrounds/PANTALLA DE CARGA.png");

        startButton = new JButton("Comenzar");
        startButton.setBounds(412, 500, 200, 50);
        startButton.addActionListener(e -> handleStartButton());
        add(startButton);
    }

    private void handleStartButton() {
        GameController.getInstance().switchScreen(new MainMenu());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Draw a fallback background if image fails to load
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString("Loading Screen", getWidth()/2 - 50, getHeight()/2);
        }
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