package com.yurwar.thread.war.threadwar;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class EnemyCreator implements Runnable {
    private final Pane root;

    public EnemyCreator(Pane root) {
        this.root = root;
    }

    @Override
    public void run() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int xPos = random.nextInt(30, ThreadWarApp.GAME_WIDTH - 30 + 1);
        Sprite enemy = new Sprite(xPos, 0, 30, 30, "enemy", Color.RED);


        root.getChildren().add(enemy);
    }

    public Pane getRoot() {
        return root;
    }
}
