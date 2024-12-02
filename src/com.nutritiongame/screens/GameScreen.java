package com.nutritiongame.screens;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import com.nutritiongame.GameController;
import com.nutritiongame.Screen;
import com.nutritiongame.game.*;
import com.nutritiongame.audio.SoundManager;

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
    private JPanel mainPanel;
    private JPanel centerPanel;
    private JPanel handPanel;

    // Notification System
    private JPanel notificationPanel;
    private Queue<NotificationMessage> notificationQueue;
    private javax.swing.Timer notificationTimer;

    private class NotificationMessage {
        String message;
        Color color;
        long timestamp;

        NotificationMessage(String message, Color color) {
            this.message = message;
            this.color = color;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public GameScreen(String player1Character, String player2Character) {
        this.player1Character = player1Character;
        this.player2Character = player2Character != null ? player2Character :
                "/resources/images/characters/senor_grasoso.png";
        this.isVsBot = (player2Character == null);

        // Initialize game components
        this.currentRound = 1;
        this.isPlayer1Turn = true;
        this.currentNutrientChallenge = "proteinas";

        this.deck = new CardDeck();
        this.player1Hand = new ArrayList<>();
        this.player2Hand = new ArrayList<>();
        this.scoreSystem = new ScoreSystem();
        this.devil = new DevilCharacter();
        this.notificationQueue = new LinkedList<>();

        // Initial character images
        updateCharacterImages();

        setPreferredSize(new Dimension(1024, 768));
        setLayout(new BorderLayout());

        initializeGame();
    }

    private void initializeGame() {
        loadImages();
        setupInitialHands();
        setupGameComponents();
        setupNotificationSystem();
        showInitialNotifications();

        // Play game start sound
        SoundManager.getInstance().playSound("game_start");
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

        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGameElements(g);
            }
        };
        mainPanel.setOpaque(false);

        // Info Panel (Top)
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Center Panel
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Hand Panel (Bottom)
        handPanel = createHandPanel();
        mainPanel.add(handPanel, BorderLayout.SOUTH);

        add(mainPanel);
        revalidate();
        repaint();
    }

    private void setupNotificationSystem() {
        notificationPanel = new JPanel();
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
        notificationPanel.setOpaque(false);
        centerPanel.add(notificationPanel);

        notificationTimer = new javax.swing.Timer(3000, e -> {
            if (!notificationQueue.isEmpty() &&
                    System.currentTimeMillis() - notificationQueue.peek().timestamp > 3000) {
                notificationQueue.poll();
                updateNotificationPanel();
            }
        });
        notificationTimer.start();
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 150));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        roundLabel = new JLabel("Ronda " + currentRound);
        player1ScoreLabel = new JLabel("Jugador 1: " + scoreSystem.getPlayer1Score());
        player2ScoreLabel = new JLabel((isVsBot ? "Bot: " : "Jugador 2: ") + scoreSystem.getPlayer2Score());
        turnLabel = new JLabel("Turno: " + (isPlayer1Turn ? "Jugador 1" : (isVsBot ? "Bot" : "Jugador 2")));
        challengeLabel = new JLabel(getCurrentChallengeText());

        Component[] labels = {roundLabel, player1ScoreLabel, player2ScoreLabel, turnLabel, challengeLabel};
        for (Component label : labels) {
            ((JLabel)label).setFont(new Font("Arial", Font.BOLD, 14));
            ((JLabel)label).setForeground(Color.WHITE);
            panel.add(label);
        }

        return panel;
    }

    private JPanel createHandPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 150));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        List<Card> currentHand = isPlayer1Turn ? player1Hand : player2Hand;
        for (int i = 0; i < currentHand.size(); i++) {
            JButton cardButton = createCardButton(currentHand.get(i), i);
            panel.add(cardButton);
        }

        return panel;
    }

    private void drawGameElements(Graphics g) {
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

        // Draw devil last so it appears on top
        devil.render(g);
    }

    private JButton createCardButton(Card card, int index) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(120, 180));

        ImageIcon cardIcon = new ImageIcon(card.getImage());
        Image scaledImage = cardIcon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            SoundManager.getInstance().playSound("card_play");
            handleCardPlay(card, isPlayer1Turn, index);
        });

        button.setToolTipText(String.format("%s\nProteínas: %d\nVitaminas: %d\nCarbohidratos: %d",
                card.getName(),
                card.getNutrientValue("proteins"),
                card.getNutrientValue("vitamins"),
                card.getNutrientValue("carbs")));

        return button;
    }

    private void showNotification(String message, Color color) {
        notificationQueue.offer(new NotificationMessage(message, color));
        while (notificationQueue.size() > 3) {
            notificationQueue.poll();
        }
        updateNotificationPanel();
    }

    private void updateNotificationPanel() {
        notificationPanel.removeAll();
        for (NotificationMessage msg : notificationQueue) {
            JLabel label = new JLabel(msg.message, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setForeground(msg.color);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);

            label.setOpaque(true);
            label.setBackground(new Color(0, 0, 0, 100));
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            notificationPanel.add(label);
            notificationPanel.add(Box.createVerticalStrut(5));
        }
        notificationPanel.revalidate();
        notificationPanel.repaint();
    }

    private void handleCardPlay(Card card, boolean isPlayer1, int cardIndex) {
        // Calculate and add score
        int score = calculateScore(card);
        scoreSystem.addScore(isPlayer1, score);

        // Update character images based on new scores
        updateCharacterImages();

        String playerName = isPlayer1 ? "Jugador 1" : (isVsBot ? "Señor Grasoso" : "Jugador 2");
        showNotification(playerName + " jugó " + card.getName(), Color.WHITE);
        showNotification("¡+" + score + " puntos!",
                score > 0 ? new Color(0, 255, 0) : new Color(255, 0, 0));

        List<Card> hand = isPlayer1 ? player1Hand : player2Hand;
        hand.remove(cardIndex);
        Card newCard = deck.drawCard();
        if (newCard != null) {
            hand.add(newCard);
        }

        isPlayer1Turn = !isPlayer1Turn;
        showNotification("Turno de " + (isPlayer1Turn ? "Jugador 1" : (isVsBot ? "Bot" : "Jugador 2")),
                Color.YELLOW);

        // Try to make the devil appear with current game context
        devil.tryAppear(currentNutrientChallenge, isPlayer1Turn ? player1Hand : player2Hand);

        updateGameState();

        if (isVsBot && !isPlayer1Turn) {
            javax.swing.Timer botTimer = new javax.swing.Timer(1000, e -> makeBotPlay());
            botTimer.setRepeats(false);
            botTimer.start();
        }

        if (scoreSystem.getRoundCount() >= 6) {
            endRound();
        }
    }

    private void makeBotPlay() {
        if (!player2Hand.isEmpty()) {
            Card bestCard = findBestCard(player2Hand);
            handleCardPlay(bestCard, false, player2Hand.indexOf(bestCard));
        }
    }

    private Card findBestCard(List<Card> hand) {
        return hand.stream()
                .max((c1, c2) -> calculateScore(c1) - calculateScore(c2))
                .orElse(hand.get(0));
    }

    private void updateGameState() {
        roundLabel.setText("Ronda " + currentRound);
        player1ScoreLabel.setText("Jugador 1: " + scoreSystem.getPlayer1Score());
        player2ScoreLabel.setText((isVsBot ? "Bot: " : "Jugador 2: ") + scoreSystem.getPlayer2Score());
        turnLabel.setText("Turno: " + (isPlayer1Turn ? "Jugador 1" : (isVsBot ? "Bot" : "Jugador 2")));
        challengeLabel.setText(getCurrentChallengeText());

        mainPanel.remove(handPanel);
        handPanel = createHandPanel();
        mainPanel.add(handPanel, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();
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

    private void showInitialNotifications() {
        showNotification("¡Comienza el juego!", Color.YELLOW);
        showNotification("Ronda 1: " + getCurrentChallengeText(), Color.CYAN);
        showNotification("Turno del Jugador 1", Color.WHITE);
    }

    private void endRound() {
        SoundManager.getInstance().playSound("round_end");

        currentRound++;
        if (currentRound > 3) {
            endGame();
        } else {
            String[] challenges = {"proteinas", "vitaminas", "carbohidratos"};
            currentNutrientChallenge = challenges[currentRound - 1];
            showNotification("¡Ronda " + currentRound + "!", Color.CYAN);
            showNotification(getCurrentChallengeText(), Color.CYAN);

            SoundManager.getInstance().playSound("round_start");
            updateGameState();
        }
    }

    private void endGame() {
        cleanup();

        SoundManager.getInstance().playSound("victory");

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

    private void cleanup() {
        if (notificationTimer != null && notificationTimer.isRunning()) {
            notificationTimer.stop();
        }
        devil.cleanup();
    }
    @Override
    public void render() {
        repaint();
    }

    @Override
    public void update() {
        // Update animation states if needed
        if (notificationPanel != null) {
            updateNotificationPanel();
        }
    }

    @Override
    public void handleInput() {
        // Handle any additional input if needed
    }

    // Helper method for debugging if needed
    private void debug(String message) {
        System.out.println("[GameScreen] " + message);
    }
    private Image getCharacterImageForScore(String baseCharacterPath, int score, boolean isBot) {
        if (isBot) {
            return GameController.getInstance().loadImage("/resources/images/characters/senor_grasoso.png");
        }

        // Get base directory name
        String characterType = baseCharacterPath.contains("nina_") ? "Nina" : "Nino";
        String baseName = new File(baseCharacterPath).getName();

        // Determine state based on score
        if (score >= 200) {
            // Strong state 2
            return GameController.getInstance().loadImage(
                    "/resources/images/characters/strong_2/" + characterType + "/" + baseName);
        } else if (score >= 100) {
            // Strong state 1
            return GameController.getInstance().loadImage(
                    "/resources/images/characters/strong_1/" + characterType + "/" + baseName);
        } else {
            // Base state
            return GameController.getInstance().loadImage(baseCharacterPath);
        }
    }

    private void updateCharacterImages() {
        // Update player 1 image
        player1Image = getCharacterImageForScore(
                player1Character,
                scoreSystem.getPlayer1Score(),
                false
        );

        // Update player 2 image
        player2Image = getCharacterImageForScore(
                player2Character,
                scoreSystem.getPlayer2Score(),
                isVsBot
        );

        // Trigger repaint to show new images
        repaint();
    }
}