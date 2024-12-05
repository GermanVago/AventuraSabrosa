package com.nutritiongame.screens;

import javax.swing.*;
import java.awt.*;
import com.nutritiongame.GameController;
import com.nutritiongame.Screen;
import com.nutritiongame.audio.SoundManager;

public class LoadingScreen extends JPanel implements Screen {
    private Image backgroundImage;
    private JButton startButton;

    public LoadingScreen() {
        setLayout(null);
        backgroundImage = GameController.getInstance().loadImage("/resources/images/backgrounds/PANTALLA DE CARGA.png");

        // Create transparent button
        startButton = new JButton();
        // Make button transparent
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        // Position and size for the button to overlay the "COMENZAR" text
        startButton.setBounds(362, 490, 300, 80); // Adjust these values to match your image

        startButton.addActionListener(e -> {
            SoundManager.getInstance().playSound("button_click");
            handleStartButton();
        });

        // Add hover effect (optional)
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

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