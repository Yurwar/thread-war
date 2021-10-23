package com.yurwar.thread.war.threadwar;

import java.util.concurrent.ThreadLocalRandom;

public class EnemyRunner implements Runnable {
    private final Sprite enemy;
    private final long waitTime;

    public EnemyRunner(Sprite enemy, long waitTime) {
        this.enemy = enemy;
        this.waitTime = waitTime;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (enemy) {
                enemy.moveDown();
            }
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
