package com.yurwar.thread.war.threadwar;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sprite extends Rectangle {
  public static final int DEFAULT_STEP = 5;
  private final String type;
  private boolean dead = false;

  public Sprite(int x, int y, int w, int h, String type, Color color) {
    super(w, h, color);
    this.type = type;
    setTranslateX(x);
    setTranslateY(y);
  }

  public synchronized void moveLeft() {
    setTranslateX(getTranslateX() - DEFAULT_STEP);
  }

  public synchronized void moveRight() {
    setTranslateX(getTranslateX() + DEFAULT_STEP);
  }

  public synchronized void moveUp() {
    setTranslateY(getTranslateY() - DEFAULT_STEP);
  }

  public synchronized void moveDown() {
    setTranslateY(getTranslateY() + DEFAULT_STEP);
  }

  public boolean isDead() {
    return dead;
  }

  public void setDead(boolean dead) {
    this.dead = dead;
  }

  public String getType() {
    return type;
  }
}
