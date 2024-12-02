package com.nutritiongame.game;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.nutritiongame.GameController;
import com.nutritiongame.audio.SoundManager;

public class DevilCharacter {
    private Image devilImage;
    private boolean isVisible;
    private String currentAdvice;
    private Point position;
    private final Random random;
    private float appearanceChance;
    private long lastAppearanceTime;
    private long visibilityDuration;
    private float currentAlpha;
    private boolean isFadingIn;
    private boolean isFadingOut;

    // Advice categories based on nutrient types
    private Map<String, List<String>> badAdvice;

    // Animation properties
    private Timer animationTimer;
    private int floatOffset;
    private boolean floatingUp;

    public DevilCharacter() {
        random = new Random();
        isVisible = false;
        appearanceChance = 0.3f; // 30% chance to appear
        visibilityDuration = 5000; // 5 seconds
        currentAlpha = 0f;
        lastAppearanceTime = 0;
        position = new Point(0, 0);
        floatOffset = 0;
        floatingUp = true;

        initializeAdvice();
        loadResources();
        setupAnimation();
    }

    private void loadResources() {
        try {
            devilImage = GameController.getInstance().loadImage("/resources/images/ui/Diablo.png");
        } catch (Exception e) {
            System.err.println("Error loading devil image: " + e.getMessage());
        }
    }

    private void initializeAdvice() {
        badAdvice = new HashMap<>();

        // Protein-related bad advice
        badAdvice.put("proteinas", new ArrayList<>(List.of(
                "¡Las papas fritas tienen toda la proteína que necesitas!",
                "¡La hamburguesa es la mejor fuente de proteínas!",
                "¡Los dulces te darán más energía que la carne!",
                "¡La pizza tiene todo lo que necesitas!"
        )));

        // Vitamin-related bad advice
        badAdvice.put("vitaminas", new ArrayList<>(List.of(
                "¡Los refrescos tienen todas las vitaminas!",
                "¡El chocolate es mejor que las frutas!",
                "¡Las golosinas son mejores que las verduras!",
                "¡No necesitas frutas, come más dulces!"
        )));

        // Carbohydrate-related bad advice
        badAdvice.put("carbohidratos", new ArrayList<>(List.of(
                "¡Come más azúcar, es buena para ti!",
                "¡Las papas fritas son perfectas!",
                "¡Un refresco te dará energía instantánea!",
                "¡Los hot dogs son la mejor opción!"
        )));
    }

    private void setupAnimation() {
        animationTimer = new Timer(50, e -> {
            // Handle floating animation
            if (floatingUp) {
                floatOffset++;
                if (floatOffset >= 10) floatingUp = false;
            } else {
                floatOffset--;
                if (floatOffset <= -10) floatingUp = true;
            }

            // Handle fade in/out
            if (isFadingIn) {
                currentAlpha = Math.min(1f, currentAlpha + 0.1f);
                if (currentAlpha >= 1f) {
                    isFadingIn = false;
                }
            }

            if (isFadingOut) {
                currentAlpha = Math.max(0f, currentAlpha - 0.1f);
                if (currentAlpha <= 0f) {
                    isFadingOut = false;
                    isVisible = false;
                }
            }
        });
        animationTimer.start();
    }

    public void tryAppear(String currentChallenge, List<Card> availableCards) {
        long currentTime = System.currentTimeMillis();
        if (!isVisible && currentTime - lastAppearanceTime > 10000) { // Minimum 10 seconds between appearances
            if (random.nextFloat() < appearanceChance) {
                appear(currentChallenge, availableCards);
            }
        }
    }

    private void appear(String currentChallenge, List<Card> availableCards) {
        isVisible = true;
        isFadingIn = true;
        isFadingOut = false;
        currentAlpha = 0f;
        lastAppearanceTime = System.currentTimeMillis();

        // Position the devil randomly on screen but away from edges
        position.x = random.nextInt(600) + 200; // Between 200 and 800
        position.y = random.nextInt(300) + 150; // Between 150 and 450

        // Select appropriate bad advice based on current challenge
        selectBadAdvice(currentChallenge, availableCards);

        // Play devil appearance sound
        SoundManager.getInstance().playSound("devil_appear");

        // Set up disappearance timer
        Timer disappearTimer = new Timer((int)visibilityDuration, e -> {
            isFadingOut = true;
            ((Timer)e.getSource()).stop();
        });
        disappearTimer.setRepeats(false);
        disappearTimer.start();
    }

    private void selectBadAdvice(String currentChallenge, List<Card> availableCards) {
        List<String> adviceList = badAdvice.get(currentChallenge);
        if (adviceList != null && !adviceList.isEmpty()) {
            // Find the worst card for current challenge
            Card worstCard = findWorstCard(currentChallenge, availableCards);
            if (worstCard != null) {
                // Sometimes give specific bad advice about the worst card
                if (random.nextBoolean()) {
                    currentAdvice = "¡" + worstCard.getName() + " es tu mejor opción!";
                    return;
                }
            }
            // Otherwise give general bad advice
            currentAdvice = adviceList.get(random.nextInt(adviceList.size()));
        }
    }

    private Card findWorstCard(String challenge, List<Card> cards) {
        if (cards == null || cards.isEmpty()) return null;

        return cards.stream().min((c1, c2) -> {
            switch (challenge) {
                case "proteinas":
                    return Integer.compare(c1.getNutrientValue("proteins"),
                            c2.getNutrientValue("proteins"));
                case "vitaminas":
                    return Integer.compare(c1.getNutrientValue("vitamins"),
                            c2.getNutrientValue("vitamins"));
                case "carbohidratos":
                    return Integer.compare(c2.getNutrientValue("carbs"),
                            c1.getNutrientValue("carbs"));
                default:
                    return 0;
            }
        }).orElse(null);
    }

    public void render(Graphics g) {
        if (!isVisible || devilImage == null) return;

        // Create copy of graphics for alpha composite
        Graphics2D g2d = (Graphics2D) g.create();

        try {
            // Set alpha transparency
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentAlpha));

            // Draw devil image with floating animation
            g2d.drawImage(devilImage,
                    position.x,
                    position.y + floatOffset,
                    100, 100, null);

            // Draw speech bubble
            if (currentAdvice != null) {
                drawSpeechBubble(g2d, currentAdvice,
                        position.x + 50,
                        position.y - 30 + floatOffset);
            }
        } finally {
            g2d.dispose();
        }
    }

    private void drawSpeechBubble(Graphics2D g2d, String text, int x, int y) {
        // Set up font and get metrics
        Font font = new Font("Arial", Font.BOLD, 14);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        // Calculate bubble dimensions
        int padding = 10;
        int bubbleWidth = fm.stringWidth(text) + padding * 2;
        int bubbleHeight = fm.getHeight() + padding * 2;

        // Draw bubble background
        g2d.setColor(new Color(255, 255, 255, (int)(255 * currentAlpha)));
        g2d.fillRoundRect(x - bubbleWidth/2, y - bubbleHeight,
                bubbleWidth, bubbleHeight, 10, 10);

        // Draw bubble border
        g2d.setColor(new Color(0, 0, 0, (int)(255 * currentAlpha)));
        g2d.drawRoundRect(x - bubbleWidth/2, y - bubbleHeight,
                bubbleWidth, bubbleHeight, 10, 10);

        // Draw text
        g2d.drawString(text,
                x - bubbleWidth/2 + padding,
                y - padding - fm.getDescent());
    }

    public void cleanup() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }
}