package com.nutritiongame.screens;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.nutritiongame.GameController;
import com.nutritiongame.Screen;
import com.nutritiongame.tutorial.TutorialStep;

public class TutorialScreen extends JPanel implements Screen {
    private List<TutorialStep> tutorialSteps;
    private int currentStep;
    private Image backgroundImage;
    private JButton nextButton;
    private JButton prevButton;
    private JButton skipButton;
    private JPanel buttonPanel;
    private JPanel contentPanel;

    public TutorialScreen() {
        setLayout(new BorderLayout());
        loadImages();
        initializeTutorialSteps();
        setupComponents();
        currentStep = 0;
        updateContent();
    }

    private void loadImages() {
        backgroundImage = GameController.getInstance().loadImage("/resources/images/backgrounds/fondo_ladrillo_blanco_final.png");
    }

    private void initializeTutorialSteps() {
        tutorialSteps = new ArrayList<>();

        // Step 1: Welcome
        tutorialSteps.add(new TutorialStep(
                "¡Bienvenido al Juego de Nutrición!",
                "Aprende sobre alimentación saludable mientras juegas y te diviertes.",
                "/resources/images/tutorial/tutorial_welcome.png"
        ));

        // Step 2: Card Types
        tutorialSteps.add(new TutorialStep(
                "Tipos de Cartas",
                "Hay tres tipos de cartas:\n" +
                        "- Cartas de Frutas (verdes)\n" +
                        "- Cartas de Verduras (azules)\n" +
                        "- Cartas de Comida Chatarra (rojas)",
                "/resources/images/tutorial/tutorial_cards.png"
        ));

        // Step 3: Nutrient Values
        tutorialSteps.add(new TutorialStep(
                "Valores Nutricionales",
                "Cada carta tiene tres valores:\n" +
                        "- Proteínas\n" +
                        "- Vitaminas\n" +
                        "- Carbohidratos",
                "/resources/images/tutorial/tutorial_nutrients.png"
        ));

        // Step 4: Gameplay
        tutorialSteps.add(new TutorialStep(
                "Cómo Jugar",
                "En cada ronda tendrás un objetivo nutricional diferente.\n" +
                        "Elige las cartas que mejor cumplan el objetivo para ganar puntos.",
                "/resources/images/tutorial/tutorial_gameplay.png"
        ));

        // Step 5: Devil Character
        tutorialSteps.add(new TutorialStep(
                "¡Cuidado con el Diablito!",
                "El diablito aparecerá para darte malos consejos.\n" +
                        "¡No lo escuches si quieres ganar!",
                "/resources/images/tutorial/tutorial_devil.png"
        ));

        // Step 6: Scoring
        tutorialSteps.add(new TutorialStep(
                "Sistema de Puntuación",
                "Ganarás puntos según qué tan bien cumplas el objetivo de la ronda.\n" +
                        "El jugador con más puntos al final del juego gana.",
                "/resources/images/tutorial/tutorial_scoring.png"
        ));
    }

    private void setupComponents() {
        // Content Panel
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);

        // Button Panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        prevButton = new JButton("Anterior");
        nextButton = new JButton("Siguiente");
        skipButton = new JButton("Saltar Tutorial");

        prevButton.addActionListener(e -> previousStep());
        nextButton.addActionListener(e -> nextStep());
        skipButton.addActionListener(e -> skipTutorial());

        buttonPanel.add(prevButton);
        buttonPanel.add(skipButton);
        buttonPanel.add(nextButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateContent() {
        contentPanel.removeAll();

        TutorialStep step = tutorialSteps.get(currentStep);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel(step.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        contentPanel.add(titleLabel, gbc);

        // Image
        if (step.getImage() != null) {
            JLabel imageLabel = new JLabel(new ImageIcon(step.getImage()));
            gbc.gridy = 1;
            contentPanel.add(imageLabel, gbc);
        }

        // Description
        JTextArea descArea = new JTextArea(step.getDescription());
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setOpaque(false);
        descArea.setEditable(false);
        descArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descArea.setForeground(Color.WHITE);
        gbc.gridy = 2;
        contentPanel.add(descArea, gbc);

        // Progress indicator
        JLabel progressLabel = new JLabel(String.format("Paso %d de %d",
                currentStep + 1, tutorialSteps.size()), SwingConstants.CENTER);
        progressLabel.setForeground(Color.WHITE);
        gbc.gridy = 3;
        contentPanel.add(progressLabel, gbc);

        // Update button states
        prevButton.setEnabled(currentStep > 0);
        nextButton.setText(currentStep == tutorialSteps.size() - 1 ? "Finalizar" : "Siguiente");

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void previousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateContent();
        }
    }

    private void nextStep() {
        if (currentStep < tutorialSteps.size() - 1) {
            currentStep++;
            updateContent();
        } else {
            finishTutorial();
        }
    }

    private void skipTutorial() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres saltar el tutorial?",
                "Saltar Tutorial",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            finishTutorial();
        }
    }

    private void finishTutorial() {
        GameController.getInstance().switchScreen(new MainMenu());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
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