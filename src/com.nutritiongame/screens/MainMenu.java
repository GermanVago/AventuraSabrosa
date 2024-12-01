package com.nutritiongame.screens;

import com.nutritiongame.GameController;
import com.nutritiongame.Screen;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel implements Screen {
    private final Image backgroundImage;

    public MainMenu() {
        setLayout(null);
        backgroundImage = GameController.getInstance().loadImage("resources/images/backgrounds/fondo_ladrillo_blanco_final.png");

        JButton tutorialButton = new JButton("TUTORIAL");
        tutorialButton.setBounds(412, 300, 200, 50);
        tutorialButton.addActionListener(e -> GameController.getInstance().switchScreen(new TutorialScreen()));
        add(tutorialButton);
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
