package com.nutritiongame.screens;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private JCheckBox fullscreenCheckbox;
    private JButton backButton;
    private JButton applyButton;

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
        // Set initial values from SoundManager
        musicVolumeSlider.setValue((int)(SoundManager.getInstance().getMusicVolume() * 100));
        effectsVolumeSlider.setValue((int)(SoundManager.getInstance().getEffectsVolume() * 100));

        // Set initial fullscreen state
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        fullscreenCheckbox.setSelected(frame != null && frame.isUndecorated());
    }

    private void setupComponents() {
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Opciones", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
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

        // Buttons panel
        JPanel buttonPanel = createButtonPanel();

        // Wrap content in center panel
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(contentPanel);

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
        musicLabel.setForeground(Color.WHITE);
        musicValueLabel = new JLabel("50%");
        musicValueLabel.setForeground(Color.WHITE);
        musicVolumeSlider = new JSlider(0, 100, 50);
        musicVolumeSlider.setOpaque(false);
        addSliderVisualFeedback(musicVolumeSlider, musicValueLabel, true);
        musicPanel.add(musicLabel);
        musicPanel.add(musicVolumeSlider);
        musicPanel.add(musicValueLabel);

        // Effects volume
        JPanel effectsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        effectsPanel.setOpaque(false);
        JLabel effectsLabel = new JLabel("Efectos:");
        effectsLabel.setForeground(Color.WHITE);
        effectsValueLabel = new JLabel("50%");
        effectsValueLabel.setForeground(Color.WHITE);
        effectsVolumeSlider = new JSlider(0, 100, 50);
        effectsVolumeSlider.setOpaque(false);
        addSliderVisualFeedback(effectsVolumeSlider, effectsValueLabel, false);
        effectsPanel.add(effectsLabel);
        effectsPanel.add(effectsVolumeSlider);
        effectsPanel.add(effectsValueLabel);

        panel.add(musicPanel);
        panel.add(effectsPanel);

        return panel;
    }

    private void addSliderVisualFeedback(JSlider slider, JLabel valueLabel, boolean isMusic) {
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();
                valueLabel.setText(value + "%");

                // Update volume in real-time
                float volume = value / 100f;
                if (isMusic) {
                    SoundManager.getInstance().setMusicVolume(volume);
                } else {
                    SoundManager.getInstance().setEffectsVolume(volume);
                    if (!slider.getValueIsAdjusting()) {
                        // Play test sound when slider is released
                        SoundManager.getInstance().playSound("button_click");
                    }
                }
            }
        });
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

        // Fullscreen option
        JPanel fullscreenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fullscreenPanel.setOpaque(false);
        fullscreenCheckbox = new JCheckBox("Pantalla Completa");
        fullscreenCheckbox.setForeground(Color.WHITE);
        fullscreenCheckbox.setOpaque(false);
        fullscreenPanel.add(fullscreenCheckbox);

        panel.add(fullscreenPanel);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);

        applyButton = new JButton("Aplicar");
        backButton = new JButton("Volver");

        styleButton(applyButton);
        styleButton(backButton);

        applyButton.addActionListener(e -> applySettings());
        backButton.addActionListener(e -> {
            SoundManager.getInstance().playSound("button_click");
            GameController.getInstance().switchScreen(new MainMenu());
        });

        panel.add(applyButton);
        panel.add(backButton);

        return panel;
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(51, 51, 51));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
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

    private void toggleFullscreen(boolean fullscreen) {
        try {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();

            if (fullscreen) {
                frame.dispose();
                frame.setUndecorated(true);
                gd.setFullScreenWindow(frame);
            } else {
                frame.dispose();
                frame.setUndecorated(false);
                gd.setFullScreenWindow(null);
                frame.setSize(1024, 768);
                frame.setLocationRelativeTo(null);
            }
            frame.setVisible(true);
        } catch (Exception e) {
            System.err.println("Error toggling fullscreen: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al cambiar el modo de pantalla",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applySettings() {
        try {
            // Apply volume settings
            float musicVolume = musicVolumeSlider.getValue() / 100f;
            float effectsVolume = effectsVolumeSlider.getValue() / 100f;
            SoundManager.getInstance().setMusicVolume(musicVolume);
            SoundManager.getInstance().setEffectsVolume(effectsVolume);

            // Apply fullscreen setting
            toggleFullscreen(fullscreenCheckbox.isSelected());

            // Play confirmation sound
            SoundManager.getInstance().playSound("button_click");

            // Show success message
            JOptionPane.showMessageDialog(this,
                    "Configuración aplicada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error applying settings: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al aplicar la configuración",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            // Add semi-transparent overlay
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