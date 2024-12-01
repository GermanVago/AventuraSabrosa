package com.nutritiongame.animation;

import java.awt.*;
import java.util.function.Consumer;

public class Animation {
    private long startTime;
    private long duration;
    private boolean isRunning;
    private Consumer<Float> updateFunction;
    private Runnable completionHandler;
    
    public Animation(long duration, Consumer<Float> updateFunction) {
        this.duration = duration;
        this.updateFunction = updateFunction;
        this.isRunning = false;
    }
    
    public void start() {
        startTime = System.currentTimeMillis();
        isRunning = true;
    }
    
    public void update() {
        if (!isRunning) return;
        
        long currentTime = System.currentTimeMillis();
        float progress = (currentTime - startTime) / (float) duration;
        
        if (progress >= 1.0f) {
            progress = 1.0f;
            isRunning = false;
            if (completionHandler != null) {
                completionHandler.run();
            }
        }
        
        updateFunction.accept(progress);
    }
    
    public void setCompletionHandler(Runnable handler) {
        this.completionHandler = handler;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
}

