package com.nutritiongame.game;

public class ScoreSystem {
    private int player1Score;
    private int player2Score;
    private int roundCount;
    
    public ScoreSystem() {
        player1Score = 0;
        player2Score = 0;
        roundCount = 0;
    }
    
    public void addScore(boolean isPlayer1, int points) {
        if (isPlayer1) {
            player1Score += points;
        } else {
            player2Score += points;
        }
        roundCount++;
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
}