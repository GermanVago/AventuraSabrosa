package com.nutritiongame.animation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
    private List<Animation> activeAnimations;
    
    public AnimationManager() {
        activeAnimations = new ArrayList<>();
    }
    
    public void update() {
        List<Animation> completedAnimations = new ArrayList<>();
        
        for (Animation animation : activeAnimations) {
            animation.update();
            if (!animation.isRunning()) {
                completedAnimations.add(animation);
            }
        }
        
        activeAnimations.removeAll(completedAnimations);
    }
    
    public void addCardPlayAnimation(Point start, Point end, Image cardImage, Runnable completion) {
        Animation animation = new Animation(500, progress -> {
            Point current = new Point(
                (int) (start.x + (end.x - start.x) * progress),
                (int) (start.y + (end.y - start.y) * progress)
            );
            // Update card position in game screen
        });
        
        animation.setCompletionHandler(completion);
        activeAnimations.add(animation);
        animation.start();
    }
    
    public void addCharacterTransformAnimation(Image startImage, Image endImage, Point position, Runnable completion) {
        Animation animation = new Animation(1000, progress -> {
            // Blend between start and end images based on progress
            // Update character appearance in game screen
        });
        
        animation.setCompletionHandler(completion);
        activeAnimations.add(animation);
        animation.start();
    }
    
    public void addVictoryAnimation(Point position, Runnable completion) {
        Animation animation = new Animation(2000, progress -> {
            // Create particle effects or other victory visuals
        });
        
        animation.setCompletionHandler(completion);
        activeAnimations.add(animation);
        animation.start();
    }
}