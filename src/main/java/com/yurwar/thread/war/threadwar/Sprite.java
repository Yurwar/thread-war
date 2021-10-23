package com.yurwar.thread.war.threadwar;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sprite extends Rectangle {
    public static final int DEFAULT_STEP = 5;
    private boolean dead = false;
    private final String type;

    public Sprite(int x, int y, int w, int h, String type, Color color) {
        super(w, h, color);
        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }

    public void moveLeft() {
        setTranslateX(getTranslateX() - DEFAULT_STEP);
    }

    public void moveRight() {
        setTranslateX(getTranslateX() + DEFAULT_STEP);
    }

    public void moveUp() {
        setTranslateY(getTranslateY() - DEFAULT_STEP);
    }

    public void moveDown() {
        setTranslateY(getTranslateY() + DEFAULT_STEP);
    }

    public boolean isDead() {
        return dead;
    }

    public String getType() {
        return type;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
