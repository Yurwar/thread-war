package com.yurwar.thread.war.threadwar;

import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class EnemyCreator extends Thread {
  private boolean spawnStarted = false;
  private int spawnCounter;
  private final ThreadWarContext context;

  public EnemyCreator(ThreadWarContext context) {
    this.context = context;
  }

  @Override
  public void run() {
    for (int i = 0; i < 15; i++) {
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (spawnStarted) {
        spawnEnemy(ThreadWarContext.GAME_WIDTH / 2);
        break;
      }
    }

    spawnStarted = true;
    while (spawnStarted) {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      if (random.nextInt(0, 101) <= spawnCounter) {
        int xPos = random.nextInt(30, ThreadWarContext.GAME_WIDTH - 30 + 1);
        spawnEnemy(xPos);
      }
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      spawnCounter += 1;
    }
  }

  private void spawnEnemy(int xPos) {
    Sprite enemy = new Sprite(xPos, 0, 30, 30, "enemy", Color.RED);
    new Thread(new EnemyRunner(context, enemy, calculateSpeed())).start();
  }

  private long calculateSpeed() {
    return 150L + (300L - spawnCounter * 2L);
  }

  public boolean isSpawnStarted() {
    return spawnStarted;
  }

  public void setSpawnStarted(boolean spawnStarted) {
    this.spawnStarted = spawnStarted;
  }
}
