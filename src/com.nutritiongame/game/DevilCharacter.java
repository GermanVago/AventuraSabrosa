package com.nutritiongame.game;

import java.awt.*;
import java.util.Random;
import java.util.List;
import java.util.Arrays;
import com.nutritiongame.GameController;

import javax.swing.*;

public class DevilCharacter {
    private Image devilImage;
    private boolean isVisible;
    private String currentAdvice;
    private Point position;
    private final Random random;
    private final List<String> badAdvice;

    public DevilCharacter() {
        // Updated path for the devil image
        devilImage = GameController.getInstance().loadImage("/images/ui/diablito.png");
        random = new Random();
        isVisible = false;
        position = new Point(0, 0);

        badAdvice = Arrays.asList(
                "¡La soda te dará súper poderes!",
                "¡El chocolate es la mejor fuente de energía!",
                "¡Las hamburguesas son mejores que las verduras!",
                "¡Pizza es vida, pizza es amor!",
                "¡Los hot dogs son el alimento perfecto!",
                "¡Las verduras son para los débiles!",
                "¡Olvida el agua, la soda es mejor!",
                "¡Entre más azúcar, mejor!"
        );
    }
    
    public void tryAppear() {
        if (random.nextDouble() < 0.3) { // 30% chance to appear
            appear();
        }
    }
    
    private void appear() {
        isVisible = true;
        currentAdvice = badAdvice.get(random.nextInt(badAdvice.size()));
        position.x = random.nextInt(400) + 200;
        position.y = random.nextInt(300) + 100;
        
        // Start disappear timer
        new Timer(3000, e -> {
            isVisible = false;
            ((Timer)e.getSource()).stop();
        }).start();
    }
    
    public void render(Graphics g) {
        if (!isVisible) return;
        
        g.drawImage(devilImage, position.x, position.y, 100, 100, null);
        
        // Draw speech bubble
        g.setColor(Color.WHITE);
        g.fillRoundRect(position.x - 100, position.y - 50, 200, 40, 10, 10);
        g.setColor(Color.BLACK);
        g.drawRoundRect(position.x - 100, position.y - 50, 200, 40, 10, 10);
        
        // Draw text
        g.setFont(new Font("Arial", Font.BOLD, 12));
        drawWrappedText(g, currentAdvice, position.x - 90, position.y - 35, 180);
    }
    
    private void drawWrappedText(Graphics g, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g.getFontMetrics();
        String[] words = text.split(" ");
        String line = "";
        
        for (String word : words) {
            String temp = line + word + " ";
            if (fm.stringWidth(temp) < maxWidth) {
                line = temp;
            } else {
                g.drawString(line, x, y);
                y += fm.getHeight();
                line = word + " ";
            }
        }
        g.drawString(line, x, y);
    }
}