package com.nutritiongame.screens;

import com.nutritiongame.GameController;
import com.nutritiongame.Screen;
import com.nutritiongame.audio.SoundManager;
import com.nutritiongame.game.Card;
import com.nutritiongame.game.CardDeck;
import com.nutritiongame.game.DevilCharacter;
import com.nutritiongame.game.ScoreSystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class GameScreen extends JPanel implements Screen {
    private final String player1Character;
    private final String player2Character;
    private final CardDeck deck;
    private final List<Card> player1Hand;
    private final List<Card> player2Hand;
    private final ScoreSystem scoreSystem;
    private final DevilCharacter devil;
    private final SoundManager soundManager;
    private Image backgroundImage;
    private Image player1Image;
    private Image player2Image;
    private int currentRound = 1;
    private boolean isPlayer1Turn = true;
    private String currentNutrientChallenge;
    
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
        soundManager = new SoundManager();
        
        setupGameComponents();
        startRound();
    }
    
    private void loadImages() {
        backgroundImage = GameController.getInstance().loadImage("/resources/game_background.png");
        player1Image = GameController.getInstance().loadImage("/resources/characters/" + player1Character);
        player2Image = GameController.getInstance().loadImage("/resources/characters/" + player2Character);
    }
    
    private void setupGameComponents() {
        // Setup player hands
        setupPlayerHand(player1Hand, true);
        setupPlayerHand(player2Hand, false);
        
        // Setup score displays
        JLabel player1Score = new JLabel("Jugador 1: 0");
        player1Score.setBounds(50, 20, 100, 30);
        add(player1Score);
        
        JLabel player2Score = new JLabel("Jugador 2: 0");
        player2Score.setBounds(getWidth() - 150, 20, 100, 30);
        add(player2Score);
        
        // Setup round information
        JLabel roundInfo = new JLabel("Ronda 1");
        roundInfo.setBounds(getWidth() / 2 - 50, 20, 100, 30);
        add(roundInfo);
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
        
        soundManager.playCardSound();
        animateCardPlay(card, isPlayer1, cardIndex);
        
        // Calculate score based on current nutrient challenge
        int score = calculateScore(card);
        scoreSystem.addScore(isPlayer1, score);
        
        // Remove played card and draw new one
        List<Card> hand = isPlayer1 ? player1Hand : player2Hand;
        hand.remove(cardIndex);
        Card newCard = deck.drawCard();
        if (newCard != null) {
            hand.add(newCard);
        }
        
        isPlayer1Turn = !isPlayer1Turn;
        updateUI();
        
        // Check if round is complete
        if (scoreSystem.getRoundCount() >= 3) {
            endRound();
        }
    }
    
    private void animateCardPlay(Card card, boolean isPlayer1, int cardIndex) {
        // Implement card play animation
        // This would involve creating a smooth movement animation from the hand to the center
    }
    
    private int calculateScore(Card card) {
        switch (currentNutrientChallenge) {
            case "proteins":
                return card.getNutrientValue("proteins") * 10;
            case "vitamins":
                return card.getNutrientValue("vitamins") * 10;
            case "carbs":
                return 100 - (card.getNutrientValue("carbs") * 5);
            default:
                return 0;
        }
    }
    
    private void startRound() {
        String[] challenges = {"proteins", "vitamins", "carbs"};
        currentNutrientChallenge = challenges[currentRound - 1];
        devil.tryAppear();
        soundManager.playRoundStartSound();
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
        JOptionPane.showMessageDialog(this, 
            "¡Juego terminado! " + (player1Wins ? "¡Jugador 1 gana!" : "¡Jugador 2 gana!"));
        GameController.getInstance().switchScreen((Screen) new MainMenu());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(player1Image, 50, 100, 200, 300, this);
        g.drawImage(player2Image, getWidth() - 250, 100, 200, 300, this);
        
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