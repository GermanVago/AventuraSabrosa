package com.nutritiongame.screens;

import javax.swing.*;
import java.awt.*;
import com.nutritiongame.GameController;
import com.nutritiongame.Screen;

public class MainMenu extends JPanel implements Screen {
    private Image backgroundImage;
    private JButton[] buttons;
    private String[] buttonLabels = {"INICIAR", "OPCIONES", "TUTORIAL"};

    public MainMenu() {
        setLayout(null);
        // Updated path according to our file structure
        backgroundImage = GameController.getInstance().loadImage("resources/images/backgrounds/PANTALLA DE CARGA.png");

        buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setBounds(412, 300 + (i * 70), 200, 50);
            final int index = i;
            buttons[i].addActionListener(e -> handleButtonClick(index));
            add(buttons[i]);
        }
    }

    private void handleButtonClick(int buttonIndex) {
        switch (buttonIndex) {
            case 0: // INICIAR
                showGameModeSelection();
                break;
            case 1: // OPCIONES
                // Implement options screen
                break;
            case 2: // TUTORIAL
                GameController.getInstance().switchScreen(new TutorialScreen());
                break;
        }
    }

    private void showGameModeSelection() {
        JDialog modeDialog = new JDialog();
        modeDialog.setLayout(new GridLayout(2, 1));

        JButton pvpButton = new JButton("1 VS 1");
        JButton botButton = new JButton("1 VS BOT");

        pvpButton.addActionListener(e -> {
            modeDialog.dispose();
            GameController.getInstance().switchScreen(new CharacterSelection(true));
        });

        botButton.addActionListener(e -> {
            modeDialog.dispose();
            GameController.getInstance().switchScreen(new CharacterSelection(false));
        });

        modeDialog.add(pvpButton);
        modeDialog.add(botButton);
        modeDialog.pack();
        modeDialog.setLocationRelativeTo(this);
        modeDialog.setVisible(true);
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