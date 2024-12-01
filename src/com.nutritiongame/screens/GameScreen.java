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
    private final String player1Character;
    private final String player2Character;
    private final CardDeck deck;
    private final List<Card> player1Hand;
    private final List<Card> player2Hand;
    private final ScoreSystem scoreSystem;
    private final DevilCharacter devil;
    private Image backgroundImage;
    private Image player1Image;
    private Image player2Image;
    private int currentRound = 1;
    private boolean isPlayer1Turn = true;
    private String currentNutrientChallenge;
    private JLabel roundLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JLabel turnLabel;
    private JLabel challengeLabel;

    public GameScreen(String player1Character, String player2Character) {
        this.player1Character = player1Character;
        this.player2Character = player2Character;

        setLayout(null);
        loadImages();

        deck = new CardDeck();
        deck.shuffle();

        player1Hand = deck.drawCards(5);
        player2Hand = deck.drawCards(5);

        scoreSystem = new ScoreSystem();
        devil = new DevilCharacter();

        setupGameComponents();
        startRound();
    }

    private void loadImages() {
        // Load background and character images using repository paths
        backgroundImage = GameController.getInstance().loadImage("/resources/images/backgrounds/game_background.png");
        player1Image = GameController.getInstance().loadImage(player1Character);
        player2Image = GameController.getInstance().loadImage(player2Character);
    }

    private void setupGameComponents() {
        // Setup round label
        roundLabel = new JLabel("Ronda " + currentRound);
        roundLabel.setBounds(getWidth()/2 - 50, 20, 100, 30);
        add(roundLabel);

        // Setup score labels
        player1ScoreLabel = new JLabel("Jugador 1: 0");
        player1ScoreLabel.setBounds(50, 20, 100, 30);
        add(player1ScoreLabel);

        player2ScoreLabel = new JLabel("Jugador 2: 0");
        player2ScoreLabel.setBounds(getWidth() - 150, 20, 100, 30);
        add(player2ScoreLabel);

        // Setup turn label
        turnLabel = new JLabel("Turno: Jugador 1");
        turnLabel.setBounds(getWidth()/2 - 50, 50, 100, 30);
        add(turnLabel);

        // Setup challenge label
        challengeLabel = new JLabel("");
        challengeLabel.setBounds(getWidth()/2 - 100, 80, 200, 30);
        add(challengeLabel);

        // Setup player hands
        setupPlayerHand(player1Hand, true);
        setupPlayerHand(player2Hand, false);
    }

    private void setupPlayerHand(List<Card> hand, boolean isPlayer1) {
        int startX = isPlayer1 ? 50 : getWidth() - 350;
        int startY = getHeight() - 200;

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            JButton cardButton = new JButton(new ImageIcon(card.getImage()));
            cardButton.setBounds(startX + (i * 60), startY, 50, 80);
            final int index = i;
            cardButton.addActionListener(e -> handleCardPlay(card, isPlayer1, index));
            add(cardButton);
        }
    }

    private void handleCardPlay(Card card, boolean isPlayer1, int cardIndex) {
        if (isPlayer1 != isPlayer1Turn) {
            return;
        }

        // Calculate score based on current nutrient challenge
        int score = calculateScore(card);
        scoreSystem.addScore(isPlayer1, score);

        // Update score labels
        updateScoreLabels();

        // Remove played card and draw new one
        List<Card> hand = isPlayer1 ? player1Hand : player2Hand;
        hand.remove(cardIndex);
        Card newCard = deck.drawCard();
        if (newCard != null) {
            hand.add(newCard);
        }

        // Switch turns
        isPlayer1Turn = !isPlayer1Turn;
        turnLabel.setText("Turno: " + (isPlayer1Turn ? "Jugador 1" : "Jugador 2"));

        // Refresh the display
        removeAll();
        setupGameComponents();

        // Check if round is complete
        if (scoreSystem.getRoundCount() >= 6) { // 3 plays each
            endRound();
        }

        // Random chance for devil to appear
        if (Math.random() < 0.3) { // 30% chance
            devil.tryAppear();
        }

        repaint();
    }

    private void updateScoreLabels() {
        player1ScoreLabel.setText("Jugador 1: " + scoreSystem.getPlayer1Score());
        player2ScoreLabel.setText("Jugador 2: " + scoreSystem.getPlayer2Score());
    }

    private int calculateScore(Card card) {
        switch (currentNutrientChallenge) {
            case "proteinas":
                return card.getNutrientValue("proteins") * 10;
            case "vitaminas":
                return card.getNutrientValue("vitamins") * 10;
            case "carbohidratos":
                // Lower carbs is better
                return Math.max(0, 100 - (card.getNutrientValue("carbs") * 5));
            default:
                return 0;
        }
    }

    private void startRound() {
        String[] challenges = {"proteinas", "vitaminas", "carbohidratos"};
        currentNutrientChallenge = challenges[currentRound - 1];

        String challengeText = "Desafío: ";
        switch (currentNutrientChallenge) {
            case "proteinas":
                challengeText += "¡Consigue más proteínas!";
                break;
            case "vitaminas":
                challengeText += "¡Busca las vitaminas!";
                break;
            case "carbohidratos":
                challengeText += "¡Evita los carbohidratos!";
                break;
        }
        challengeLabel.setText(challengeText);

        roundLabel.setText("Ronda " + currentRound);
    }

    private void endRound() {
        currentRound++;
        if (currentRound > 3) {
            endGame();
        } else {
            startRound();
        }
    }

    private void endGame() {
        boolean player1Wins = scoreSystem.getPlayer1Score() > scoreSystem.getPlayer2Score();
        String message = "¡Juego terminado!\n" +
                "Jugador 1: " + scoreSystem.getPlayer1Score() + " puntos\n" +
                "Jugador 2: " + scoreSystem.getPlayer2Score() + " puntos\n\n" +
                (player1Wins ? "¡Jugador 1 gana!" : "¡Jugador 2 gana!");

        JOptionPane.showMessageDialog(this, message);
        GameController.getInstance().switchScreen(new MainMenu());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw players
        g.drawImage(player1Image, 50, 100, 200, 300, this);
        g.drawImage(player2Image, getWidth() - 250, 100, 200, 300, this);

        // Draw devil if active
        devil.render(g);
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