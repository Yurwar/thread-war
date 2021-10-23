package com.yurwar.thread.war.threadwar;

import javafx.application.Platform;

public class EnemyRunner implements Runnable {
  private final ThreadWarContext context;
  private final Sprite enemy;
  private final long waitTime;

  public EnemyRunner(ThreadWarContext context, Sprite enemy, long waitTime) {
    this.context = context;
    this.enemy = enemy;
    this.waitTime = waitTime;
  }

  @Override
  public void run() {

    Platform.runLater(() -> context.addChildren(enemy));
    while (true) {
      enemy.moveDown();
      if (enemy.getTranslateY() >= ThreadWarContext.GAME_HEIGHT) {
        Platform.runLater(
            () -> {
              context.increaseMissesStat();
              context.removeChild(enemy);
            });
        break;
      }
      try {
        Thread.sleep(waitTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
