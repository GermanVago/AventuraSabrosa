package com.nutritiongame.game;

public class ScoreSystem {
    private int player1Score;
    private int player2Score;
    private int roundCount;

    // Score thresholds for character transformations
    public static final int STRONG_1_THRESHOLD = 100;
    public static final int STRONG_2_THRESHOLD = 200;

    // Bonus points for healthy choices
    public static final int HEALTH_BONUS_THRESHOLD = 80;
    public static final int HEALTH_BONUS_POINTS = 20;

    public ScoreSystem() {
        player1Score = 0;
        player2Score = 0;
        roundCount = 0;
    }

    public void addScore(boolean isPlayer1, int basePoints, Card playedCard) {
        // Calculate total points including health bonus
        int totalPoints = calculateTotalPoints(basePoints, playedCard);

        // Add points to appropriate player
        if (isPlayer1) {
            player1Score += totalPoints;
        } else {
            player2Score += totalPoints;
        }
        roundCount++;
    }

    private int calculateTotalPoints(int basePoints, Card playedCard) {
        int totalPoints = basePoints;

        // Add bonus points for healthy choices
        if (playedCard.getHealthScore() >= HEALTH_BONUS_THRESHOLD) {
            totalPoints += HEALTH_BONUS_POINTS;
        }

        return totalPoints;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void resetScores() {
        player1Score = 0;
        player2Score = 0;
        roundCount = 0;
    }

    public void resetRoundCount() {
        roundCount = 0;
    }

    public int getCharacterState(int score) {
        if (score >= STRONG_2_THRESHOLD) return 2;
        if (score >= STRONG_1_THRESHOLD) return 1;
        return 0;
    }

    // Check if a player has progressed to a new state
    public boolean hasProgressedToNewState(int oldScore, int newScore) {
        return getCharacterState(oldScore) != getCharacterState(newScore);
    }

    // Get bonus message if applicable
    public String getBonusMessage(Card playedCard) {
        if (playedCard.getHealthScore() >= HEALTH_BONUS_THRESHOLD) {
            return "¡Bonus por elección saludable! +" + HEALTH_BONUS_POINTS + " puntos";
        }
        return null;
    }
}