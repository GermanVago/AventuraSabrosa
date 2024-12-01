package com.nutritiongame.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import com.nutritiongame.GameController;
import com.nutritiongame.Screen;
import com.nutritiongame.game.*;

public class GameScreen extends JPanel implements Screen {
    // Game state
    private int currentRound;
    private boolean isPlayer1Turn;
    private String currentNutrientChallenge;

    // Game components
    private final String player1Character;
    private final String player2Character;
    private final boolean isVsBot;
    private final CardDeck deck;
    private final List<Card> player1Hand;
    private final List<Card> player2Hand;
    private final ScoreSystem scoreSystem;
    private final DevilCharacter devil;

    // UI Components
    private Image backgroundImage;
    private Image player1Image;
    private Image player2Image;
    private JLabel roundLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JLabel turnLabel;
    private JLabel challengeLabel;
    private JPanel cardPanel;

    public GameScreen(String player1Character, String player2Character) {
        // Initialize properties
        this.player1Character = player1Character;
        this.player2Character = player2Character != null ? player2Character : "/resources/images/characters/nino_WH_SD_1.png";
        this.isVsBot = (player2Character == null);

        // Initialize game state
        this.currentRound = 1;
        this.isPlayer1Turn = true;
        this.currentNutrientChallenge = "proteinas"; // Start with proteins challenge

        // Initialize game components
        this.deck = new CardDeck();
        this.player1Hand = new ArrayList<>();
        this.player2Hand = new ArrayList<>();
        this.scoreSystem = new ScoreSystem();
        this.devil = new DevilCharacter();

        // Setup panel
        setPreferredSize(new Dimension(1024, 768));
        setLayout(new BorderLayout());

        // Initialize game
        initializeGame();
    }

    private void initializeGame() {
        loadImages();
        setupInitialHands();
        setupGameComponents();
    }

    private void loadImages() {
        try {
            backgroundImage = GameController.getInstance().loadImage("/resources/images/backgrounds/fondo_ladrillo_blanco_final.png");
            player1Image = GameController.getInstance().loadImage(player1Character);
            player2Image = GameController.getInstance().loadImage(player2Character);
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
    }

    private void setupInitialHands() {
        deck.shuffle();
        player1Hand.addAll(deck.drawCards(5));
        player2Hand.addAll(deck.drawCards(5));
    }

    private void setupGameComponents() {
        removeAll();

        // Create panels
        JPanel gameInfoPanel = createGameInfoPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel playerHandPanel = createPlayerHandPanel();

        // Add panels to frame
        add(gameInfoPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(playerHandPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel createGameInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255, 200));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        roundLabel = new JLabel("Ronda " + currentRound);
        player1ScoreLabel = new JLabel("Jugador 1: " + scoreSystem.getPlayer1Score());
        player2ScoreLabel = new JLabel((isVsBot ? "Bot: " : "Jugador 2: ") + scoreSystem.getPlayer2Score());
        turnLabel = new JLabel("Turno: " + (isPlayer1Turn ? "Jugador 1" : (isVsBot ? "Bot" : "Jugador 2")));
        challengeLabel = new JLabel(getCurrentChallengeText());

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Component[] labels = {roundLabel, player1ScoreLabel, player2ScoreLabel, turnLabel, challengeLabel};
        for (Component label : labels) {
            ((JLabel)label).setFont(labelFont);
            panel.add(label);
        }

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGameElements(g);
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private void drawGameElements(Graphics g) {
        // Draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw characters
        int characterWidth = 200;
        int characterHeight = 300;
        if (player1Image != null) {
            g.drawImage(player1Image, 50, getHeight()/2 - characterHeight/2,
                    characterWidth, characterHeight, this);
        }
        if (player2Image != null) {
            g.drawImage(player2Image, getWidth() - characterWidth - 50,
                    getHeight()/2 - characterHeight/2, characterWidth, characterHeight, this);
        }

        // Draw devil if active
        devil.render(g);
    }

    private JPanel createPlayerHandPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 100));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Add card buttons for current player's hand
        List<Card> currentHand = isPlayer1Turn ? player1Hand : player2Hand;
        for (int i = 0; i < currentHand.size(); i++) {
            JButton cardButton = createCardButton(currentHand.get(i), i);
            panel.add(cardButton);
        }

        return panel;
    }

    private JButton createCardButton(Card card, int index) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(120, 180));

        // Set card image
        ImageIcon cardIcon = new ImageIcon(card.getImage());
        Image scaledImage = cardIcon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));

        // Set button properties
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        // Add action listener
        button.addActionListener(e -> handleCardPlay(card, isPlayer1Turn, index));

        // Add tooltip
        button.setToolTipText(String.format("%s\nProteínas: %d\nVitaminas: %d\nCarbohidratos: %d",
                card.getName(),
                card.getNutrientValue("proteins"),
                card.getNutrientValue("vitamins"),
                card.getNutrientValue("carbs")));

        return button;
    }

    private void handleCardPlay(Card card, boolean isPlayer1, int cardIndex) {
        // Calculate score
        int score = calculateScore(card);
        scoreSystem.addScore(isPlayer1, score);

        // Update hand
        List<Card> hand = isPlayer1 ? player1Hand : player2Hand;
        hand.remove(cardIndex);
        Card newCard = deck.drawCard();
        if (newCard != null) {
            hand.add(newCard);
        }

        // Switch turns
        isPlayer1Turn = !isPlayer1Turn;

        // Bot play if necessary
        if (isVsBot && !isPlayer1Turn) {
            makeBotPlay();
        }

        // Update UI
        updateGameState();

        // Check for round end
        if (scoreSystem.getRoundCount() >= 6) {
            endRound();
        }
    }

    private void makeBotPlay() {
        if (!player2Hand.isEmpty()) {
            // Simple bot AI - picks the best card for current challenge
            Card bestCard = findBestCard(player2Hand);
            handleCardPlay(bestCard, false, player2Hand.indexOf(bestCard));
        }
    }

    private Card findBestCard(List<Card> hand) {
        return hand.stream()
                .max((c1, c2) -> calculateScore(c1) - calculateScore(c2))
                .orElse(hand.get(0));
    }

    private int calculateScore(Card card) {
        switch (currentNutrientChallenge) {
            case "proteinas":
                return card.getNutrientValue("proteins") * 10;
            case "vitaminas":
                return card.getNutrientValue("vitamins") * 10;
            case "carbohidratos":
                return Math.max(0, 100 - (card.getNutrientValue("carbs") * 5));
            default:
                return 0;
        }
    }

    private String getCurrentChallengeText() {
        switch (currentNutrientChallenge) {
            case "proteinas":
                return "¡Consigue más proteínas!";
            case "vitaminas":
                return "¡Busca las vitaminas!";
            case "carbohidratos":
                return "¡Evita los carbohidratos!";
            default:
                return "¡Preparando desafío!";
        }
    }

    private void updateGameState() {
        setupGameComponents();
    }

    private void endRound() {
        currentRound++;
        if (currentRound > 3) {
            endGame();
        } else {
            String[] challenges = {"proteinas", "vitaminas", "carbohidratos"};
            currentNutrientChallenge = challenges[currentRound - 1];
            updateGameState();
        }
    }

    private void endGame() {
        boolean player1Wins = scoreSystem.getPlayer1Score() > scoreSystem.getPlayer2Score();
        String message = String.format(
                "¡Juego terminado!\n\n" +
                        "Jugador 1: %d puntos\n" +
                        "%s: %d puntos\n\n" +
                        "¡%s gana!",
                scoreSystem.getPlayer1Score(),
                isVsBot ? "Bot" : "Jugador 2",
                scoreSystem.getPlayer2Score(),
                player1Wins ? "Jugador 1" : (isVsBot ? "Bot" : "Jugador 2")
        );

        JOptionPane.showMessageDialog(this, message);
        GameController.getInstance().switchScreen(new MainMenu());
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