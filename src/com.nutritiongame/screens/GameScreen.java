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

        // After initializing the deck
        this.deck = new CardDeck(isVsBot);
        this.deck.prepareForChallenge("proteinas"); // Prepare initial deck for first round
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
            ((JLabel)label).setFont(new Font("Arial", Font.BOLD, 23));
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

        // Fixed dimensions that were working before
        int characterWidth = 350;  // Base width
        int characterHeight = 400; // Base height

        // Calculate positions
        int player1X = 50;
        int player2X = getWidth() - characterWidth - 50;
        int characterY = getHeight()/2 - characterHeight/2;

        // Draw player 1
        if (player1Image != null) {
            g.drawImage(player1Image, player1X, characterY,
                    characterWidth, characterHeight, this);
        }

        // Draw player 2 (or bot)
        if (player2Image != null) {
            g.drawImage(player2Image, player2X, characterY,
                    characterWidth, characterHeight, this);
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

        // Updated tooltip to include health score and description
        button.setToolTipText(String.format("<html>%s<br>Proteínas: %d<br>Vitaminas: %d<br>Carbohidratos: %d<br>Salud: %d%%<br><i>%s</i></html>",
                card.getName(),
                card.getNutrientValue("proteins"),
                card.getNutrientValue("vitamins"),
                card.getNutrientValue("carbs"),
                card.getHealthScore(),
                card.getDescription()
        ));

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
        // Calculate base score
        int baseScore = calculateScore(card);

        // Add score with the played card
        scoreSystem.addScore(isPlayer1, baseScore, card);

        // Show who played the card
        String playerName = isPlayer1 ? "Jugador 1" : (isVsBot ? "Señor Grasoso" : "Jugador 2");
        showNotification(playerName + " jugó " + card.getName(), Color.WHITE);

        // Show points notification
        showNotification("¡+" + baseScore + " puntos!",
                baseScore > 0 ? new Color(0, 255, 0) : new Color(255, 0, 0));

        // Show bonus notification if applicable
        String bonusMessage = scoreSystem.getBonusMessage(card);
        if (bonusMessage != null) {
            showNotification(bonusMessage, new Color(255, 215, 0)); // Gold color
        }

        // Update hands
        List<Card> hand = isPlayer1 ? player1Hand : player2Hand;
        hand.remove(cardIndex);
        Card newCard = deck.drawCard();
        if (newCard != null) {
            hand.add(newCard);
        } else {
            // If deck is empty, prepare new deck with current challenge
            deck.prepareForChallenge(currentNutrientChallenge);
            newCard = deck.drawCard();
            if (newCard != null) {
                hand.add(newCard);
            }
        }

        // Switch turns
        isPlayer1Turn = !isPlayer1Turn;
        showNotification("Turno de " + (isPlayer1Turn ? "Jugador 1" : (isVsBot ? "Señor Grasoso" : "Jugador 2")),
                Color.YELLOW);

        // Try to make devil appear
        devil.tryAppear(currentNutrientChallenge, isPlayer1Turn ? player1Hand : player2Hand);

        // Update character images and game state
        updateCharacterImages();
        updateGameState();

        // Handle bot turn if applicable
        if (isVsBot && !isPlayer1Turn) {
            javax.swing.Timer botTimer = new javax.swing.Timer(1000, e -> makeBotPlay());
            botTimer.setRepeats(false);
            botTimer.start();
        }

        // Check for round end
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
            // Prepare deck for new challenge
            deck.prepareForChallenge(currentNutrientChallenge);

            showNotification("¡Ronda " + currentRound + "!", Color.CYAN);
            showNotification(getCurrentChallengeText(), Color.CYAN);

            scoreSystem.resetRoundCount();
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

        String characterType = baseCharacterPath.contains("nina_") ? "Nina" : "Nino";
        String baseName = new File(baseCharacterPath).getName();

        String imagePath;
        if (score >= 200) {
            // Strong state 2
            imagePath = "/resources/images/characters/strong_2/" + characterType + "/" + baseName;
        } else if (score >= 100) {
            // Strong state 1
            imagePath = "/resources/images/characters/strong_1/" + characterType + "/" + baseName;
        } else {
            // Base state
            imagePath = baseCharacterPath;
        }

        return GameController.getInstance().loadImage(imagePath);
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

    private Color getHealthScoreColor(Card card) {
        int score = card.getHealthScore();
        if (score >= 80) return new Color(0, 255, 0, 100); // Green for healthy
        if (score >= 50) return new Color(255, 255, 0, 100); // Yellow for moderate
        return new Color(255, 0, 0, 100); // Red for unhealthy
    }
}