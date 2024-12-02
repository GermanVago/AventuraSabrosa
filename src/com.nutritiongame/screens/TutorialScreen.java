package com.nutritiongame.screens;

import com.nutritiongame.GameController;
import com.nutritiongame.Screen;
import com.nutritiongame.tutorial.TutorialStep;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TutorialScreen extends JPanel implements Screen {
    private List<TutorialStep> steps;
    private int currentStep;
    private Image backgroundImage;
    private JButton nextButton;
    private JButton previousButton;
    private JButton skipButton;
    
    public TutorialScreen() {
        setLayout(null);
        backgroundImage = GameController.getInstance().loadImage("/resources/tutorial_background.png");
        initializeSteps();
        setupButtons();
    }
    
    private void initializeSteps() {
        steps = new ArrayList<>();
        
        steps.add(new TutorialStep(
            "Bienvenido al Juego de Nutrición",
            "¡Aprende sobre alimentación saludable mientras te diviertes!",
            "/resources/tutorial/intro.png"
        ));
        
        steps.add(new TutorialStep(
            "Las Cartas",
            "Cada carta tiene valores nutricionales:\n- Proteínas\n- Vitaminas\n- Carbohidratos",
            "/resources/tutorial/cards.png"
        ));
        
        steps.add(new TutorialStep(
            "Tipos de Cartas",
            "Hay tres tipos de cartas:\n- Comida Chatarra (rojas)\n- Frutas (verdes)\n- Verduras (azules)",
            "/resources/tutorial/card_types.png"
        ));
        
        steps.add(new TutorialStep(
            "¡Cuidado con el Diablito!",
            "El diablito aparecerá para darte malos consejos. ¡No lo escuches!",
            "/resources/tutorial/devil.png"
        ));
        
        steps.add(new TutorialStep(
            "Objetivos del Juego",
            "Cada ronda tendrás un objetivo nutricional diferente.\nElige las cartas que mejor cumplan el objetivo.",
            "/resources/tutorial/goals.png"
        ));
        
        currentStep = 0;
    }
    
    private void setupButtons() {
        previousButton = new JButton("Anterior");
        previousButton.setBounds(50, 500, 100, 30);
        previousButton.addActionListener(e -> previousStep());
        previousButton.setEnabled(false);
        add(previousButton);
        
        nextButton = new JButton("Siguiente");
        nextButton.setBounds(getWidth() - 150, 500, 100, 30);
        nextButton.addActionListener(e -> nextStep());
        add(nextButton);
        
        skipButton = new JButton("Saltar Tutorial");
        skipButton.setBounds(getWidth() / 2 - 50, 500, 120, 30);
        skipButton.addActionListener(e -> skipTutorial());
        add(skipButton);
    }
    
    private void previousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateButtons();
            repaint();
        }
    }
    
    private void nextStep() {
        if (currentStep < steps.size() - 1) {
            currentStep++;
            updateButtons();
            repaint();
        } else {
            finishTutorial();
        }
    }
    
    private void updateButtons() {
        previousButton.setEnabled(currentStep > 0);
        nextButton.setText(currentStep == steps.size() - 1 ? "Finalizar" : "Siguiente");
    }
    
    private void skipTutorial() {
        int choice = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que quieres saltar el tutorial?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            finishTutorial();
        }
    }
    
    private void finishTutorial() {
        GameController.getInstance().switchScreen(new MainMenu());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        
        TutorialStep currentTutorialStep = steps.get(currentStep);
        
        // Draw title
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.BLACK);
        drawCenteredString(g, currentTutorialStep.getTitle(), getWidth() / 2, 50);
        
        // Draw tutorial image
        Image stepImage = GameController.getInstance().loadImage(currentTutorialStep.getImagePath());
        g.drawImage(stepImage, getWidth() / 2 - 200, 100, 400, 300, this);
        
        // Draw description
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        drawMultilineText(g, currentTutorialStep.getDescription(), 100, 420, getWidth() - 200);
        
        // Draw progress indicator
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        String progress = String.format("Paso %d de %d", currentStep + 1, steps.size());
        drawCenteredString(g, progress, getWidth() / 2, 470);
    }
    
    private void drawCenteredString(Graphics g, String text, int x, int y) {
        FontMetrics metrics = g.getFontMetrics();
        x = x - metrics.stringWidth(text) / 2;
        g.drawString(text, x, y);
    }
    
    private void drawMultilineText(Graphics g, String text, int x, int y, int maxWidth) {
        FontMetrics metrics = g.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            if (metrics.stringWidth(currentLine + " " + word) < maxWidth) {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            } else {
                g.drawString(currentLine.toString(), x, y);
                y += metrics.getHeight();
                currentLine = new StringBuilder(word);
            }
        }
        g.drawString(currentLine.toString(), x, y);
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
