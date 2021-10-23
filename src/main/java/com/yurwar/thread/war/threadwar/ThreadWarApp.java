package com.yurwar.thread.war.threadwar;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class ThreadWarApp extends Application {
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 800;
    private int hits;
    private int misses;
    private double t = 0.0;
    private final Pane root = new Pane();
    private Sprite player = new Sprite(300, 750, 40, 40, "player", Color.BLUE);

    private Parent createContent() {
        root.setPrefSize(GAME_WIDTH, GAME_HEIGHT);
        root.getChildren().add(player);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        nextLevel();

        return root;
    }

    private void nextLevel() {
        for (int i = 0; i < 5; i++) {
            Sprite s = new Sprite(90 + i * 100, 150, 30, 30, "enemy", Color.RED);

            root.getChildren().add(s);
        }
    }

    private List<Sprite> sprites() {
        return root.getChildren().stream()
                .filter(s -> s instanceof Sprite)
                .map(s -> (Sprite) s)
                .collect(Collectors.toList());
    }

    private void update() {
        t += 0.016;
        sprites().forEach(s -> {
            switch (s.getType()) {
                case "enemybullet" -> {
                    s.moveDown();
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        player.setDead(true);
                        s.setDead(true);
                    }
                }
                case "playerbullet" -> {
                    s.moveUp();
                    sprites().stream().filter(e -> e.getType().equals("enemy"))
                            .forEach(enemy -> {
                                if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                                    enemy.setDead(true);
                                    s.setDead(true);
                                }
                            });
                }
                case "enemy" -> {
                    s.moveDown();
                }
            }
        });

        root.getChildren().removeIf(n -> {
            if (n instanceof Sprite s) {
                return s.isDead();
            } else {
                return false;
            }
        });
        if (t > 2) {
            t = 0;
        }
    }

    private void shoot(Sprite who) {
        Sprite s = new Sprite((int) who.getTranslateX() + 20, (int) who.getTranslateY(), 5, 20, who.getType() + "bullet", Color.BLACK);

        root.getChildren().add(s);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> player.moveUp();
                case DOWN -> player.moveDown();
                case LEFT -> player.moveLeft();
                case RIGHT -> player.moveRight();
                case ENTER, SPACE -> shoot(player);
            }
        });
        stage.setScene(scene);
        stage.setTitle("Thread War");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}