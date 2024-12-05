package com.nutritiongame.screens;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import com.nutritiongame.GameController;
import com.nutritiongame.Screen;
import com.nutritiongame.audio.SoundManager;

public class OptionsScreen extends JPanel implements Screen {
    private Image backgroundImage;
    private JSlider musicVolumeSlider;
    private JSlider effectsVolumeSlider;
    private JLabel musicValueLabel;
    private JLabel effectsValueLabel;
    private JButton backButton;
    private JPanel contentPanel;

    public OptionsScreen() {
        setLayout(new BorderLayout());
        loadImages();
        setupComponents();
        initializeValues();
    }

    private void loadImages() {
        try {
            backgroundImage = GameController.getInstance().loadImage("/resources/images/backgrounds/fondo_ladrillo_blanco_final.png");
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
    }

    private void initializeValues() {
        musicVolumeSlider.setValue((int)(SoundManager.getInstance().getMusicVolume() * 100));
        effectsVolumeSlider.setValue((int)(SoundManager.getInstance().getEffectsVolume() * 100));

        // Update labels
        musicValueLabel.setText(musicVolumeSlider.getValue() + "%");
        effectsValueLabel.setText(effectsVolumeSlider.getValue() + "%");
    }

    private void setupComponents() {
        // Main content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Opciones", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Sound options
        JPanel soundPanel = createSoundPanel();
        contentPanel.add(soundPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Visual options
        JPanel visualPanel = createVisualPanel();
        contentPanel.add(visualPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Wrap content in center panel
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(contentPanel);

        // Back button
        backButton = new JButton("Volver");
        backButton.setPreferredSize(new Dimension(200, 60));
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setFocusPainted(false);
        backButton.setBackground(new Color(51, 51, 51));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 3),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(75, 75, 75));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(51, 51, 51));
            }
        });

        backButton.addActionListener(e -> {
            SoundManager.getInstance().playSound("button_click");
            GameController.getInstance().switchScreen(new MainMenu());
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        add(centerWrapper, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createSoundPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Sonido",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE
        ));

        // Music volume
        JPanel musicPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        musicPanel.setOpaque(false);
        JLabel musicLabel = new JLabel("Música:");
        musicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        musicLabel.setForeground(Color.WHITE);
        musicValueLabel = new JLabel("50%");
        musicValueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        musicValueLabel.setForeground(Color.WHITE);

        musicVolumeSlider = new JSlider(0, 100, 50);
        musicVolumeSlider.setOpaque(false);
        musicVolumeSlider.addChangeListener(e -> {
            float volume = musicVolumeSlider.getValue() / 100f;
            SoundManager.getInstance().setMusicVolume(volume);
            musicValueLabel.setText(musicVolumeSlider.getValue() + "%");
        });

        musicPanel.add(musicLabel);
        musicPanel.add(musicVolumeSlider);
        musicPanel.add(musicValueLabel);

        // Effects volume
        JPanel effectsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        effectsPanel.setOpaque(false);
        JLabel effectsLabel = new JLabel("Efectos:");
        effectsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        effectsLabel.setForeground(Color.WHITE);
        effectsValueLabel = new JLabel("50%");
        effectsValueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        effectsValueLabel.setForeground(Color.WHITE);

        effectsVolumeSlider = new JSlider(0, 100, 50);
        effectsVolumeSlider.setOpaque(false);
        effectsVolumeSlider.addChangeListener(e -> {
            float volume = effectsVolumeSlider.getValue() / 100f;
            SoundManager.getInstance().setEffectsVolume(volume);
            effectsValueLabel.setText(effectsVolumeSlider.getValue() + "%");
            if (!effectsVolumeSlider.getValueIsAdjusting()) {
                SoundManager.getInstance().playSound("button_click");
            }
        });

        effectsPanel.add(effectsLabel);
        effectsPanel.add(effectsVolumeSlider);
        effectsPanel.add(effectsValueLabel);

        panel.add(musicPanel);
        panel.add(effectsPanel);

        return panel;
    }

    private JPanel createVisualPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Visual",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE
        ));

        // Screen mode buttons
        JPanel screenModePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        screenModePanel.setOpaque(false);

        JButton windowedButton = new JButton("Ventana");
        JButton fullscreenButton = new JButton("Pantalla Completa");

        // Style both buttons
        for (JButton button : new JButton[]{windowedButton, fullscreenButton}) {
            button.setPreferredSize(new Dimension(200, 60));
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusPainted(false);
            button.setBackground(new Color(51, 51, 51));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 3),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(75, 75, 75));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(51, 51, 51));
                }
            });
        }

        // Add actions
        windowedButton.addActionListener(e -> {
            SoundManager.getInstance().playSound("button_click");
            GameController.getInstance().toggleFullscreen(false);
        });

        fullscreenButton.addActionListener(e -> {
            SoundManager.getInstance().playSound("button_click");
            GameController.getInstance().toggleFullscreen(true);
        });

        screenModePanel.add(windowedButton);
        screenModePanel.add(fullscreenButton);
        panel.add(screenModePanel);

        return panel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 160));
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