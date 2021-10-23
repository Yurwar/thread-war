package com.yurwar.thread.war.threadwar;

import javafx.application.Platform;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public record BulletRunner(ThreadWarContext context,
                           Sprite bullet,
                           Semaphore bulletSemaphore) implements Runnable {
  private static final long BULLET_WAIT = 10L;

  @Override
  public void run() {
    if (bulletSemaphore.tryAcquire()) {
      try {
        Platform.runLater(() -> context.addChildren(bullet));

        while (true) {
          bullet.moveUp();
          if (bullet.getTranslateY() < 0) {
            Platform.runLater(() -> context.removeChild(bullet));
            break;
          }
          List<Sprite> killedEnemies =
                  context.getEnemies().stream()
                          .filter(enemy -> enemy.getBoundsInParent().intersects(bullet.getBoundsInParent()))
                          .collect(Collectors.toList());
          if (!killedEnemies.isEmpty()) {
            Platform.runLater(
                    () -> {
                      context.removeAllChildren(killedEnemies);
                      context.removeChild(bullet);
                      context.increaseHitsStat();
                    });
            break;
          }
          try {
            Thread.sleep(BULLET_WAIT);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      } finally {
        bulletSemaphore.release();
      }
    }
  }
}
