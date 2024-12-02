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
        setLayout(new BorderLayout());
        loadImages();
        setupButtons();
    }

    private void loadImages() {
        backgroundImage = GameController.getInstance().loadImage("/resources/images/backgrounds/fondo_ladrillo_blanco_final.png");
    }

    private void setupButtons() {
        // Create a panel for buttons with vertical layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = createStyledButton(buttonLabels[i]);
            final int index = i;
            buttons[i].addActionListener(e -> handleButtonClick(index));

            // Center the button and add spacing
            JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonWrapper.setOpaque(false);
            buttonWrapper.add(buttons[i]);
            buttonPanel.add(buttonWrapper);

            // Add space between buttons
            if (i < buttonLabels.length - 1) {
                buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }

        // Create wrapper panel for vertical centering
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(buttonPanel);

        add(centerWrapper, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBackground(new Color(51, 51, 51));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(75, 75, 75));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(51, 51, 51));
            }
        });

        return button;
    }

    private void handleButtonClick(int buttonIndex) {
        switch (buttonIndex) {
            case 0: // INICIAR
                showGameModeSelection();
                break;
            case 1: // OPCIONES
                // Will be implemented later
                JOptionPane.showMessageDialog(this, "Opciones próximamente",
                        "En Desarrollo", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2: // TUTORIAL
                GameController.getInstance().switchScreen(new TutorialScreen());
                break;
        }
    }

    private void showGameModeSelection() {
        JDialog modeDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Seleccionar Modo", true);
        modeDialog.setLayout(new GridBagLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setOpaque(false);

        JButton pvpButton = createStyledButton("1 VS 1");
        JButton botButton = createStyledButton("1 VS BOT");

        pvpButton.addActionListener(e -> {
            modeDialog.dispose();
            GameController.getInstance().switchScreen(new CharacterSelection(true));
        });

        botButton.addActionListener(e -> {
            modeDialog.dispose();
            GameController.getInstance().switchScreen(new CharacterSelection(false));
        });

        buttonPanel.add(pvpButton);
        buttonPanel.add(botButton);

        modeDialog.add(buttonPanel);
        modeDialog.pack();
        modeDialog.setLocationRelativeTo(this);
        modeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        modeDialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            // Add semi-transparent overlay
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
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