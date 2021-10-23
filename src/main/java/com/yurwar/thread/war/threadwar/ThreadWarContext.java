package com.yurwar.thread.war.threadwar;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ThreadWarContext {
  public static final int GAME_WIDTH = 600;
  public static final int GAME_HEIGHT = 800;
  private static final String MISSES_LABEL_FORMAT = "Misses: %d";
  public static final String HITS_LABEL_FORMAT = "Hits: %d";
  private final Pane root = new Pane();
  private final AtomicInteger hits = new AtomicInteger(0);
  private final AtomicInteger misses = new AtomicInteger(0);
  private final Sprite player = new Sprite(300, 750, 40, 40, "player", Color.BLUE);
  private final Semaphore bulletSemaphore = new Semaphore(3, true);
  private final EnemyCreator enemyCreator = new EnemyCreator(this);
  private final Label missesStat = new Label(String.format(MISSES_LABEL_FORMAT, 0));
  private final Label hitsStat = new Label(String.format(HITS_LABEL_FORMAT, 0));
  private final Scene scene;

  public ThreadWarContext(Stage stage) {
    root.setPrefSize(GAME_WIDTH, GAME_HEIGHT);
    root.getChildren().add(player);
    missesStat.setTranslateX(0);
    missesStat.setTranslateY(20);
    hitsStat.setTranslateX(0);
    hitsStat.setTranslateY(0);
    root.getChildren().add(missesStat);
    root.getChildren().add(hitsStat);

    enemyCreator.start();

    this.scene = new Scene(getRoot());
    scene.setOnKeyPressed(this::handleKeyEvent);
    stage.setScene(scene);
    stage.setTitle("Thread War");
    stage.show();
  }

  public Pane getRoot() {
    return root;
  }

  private void shoot() {
    Sprite bullet = new Sprite((int) player.getTranslateX() + 20, (int) player.getTranslateY(), 5, 20, "bullet", Color.BLACK);
    BulletRunner bulletRunner = new BulletRunner(this, bullet, bulletSemaphore);

    new Thread(bulletRunner).start();
  }

  public void handleKeyEvent(KeyEvent e) {
    if (!enemyCreator.isSpawnStarted()) {
      enemyCreator.setSpawnStarted(true);
    }
    switch (e.getCode()) {
      case UP -> player.moveUp();
      case DOWN -> player.moveDown();
      case LEFT -> player.moveLeft();
      case RIGHT -> player.moveRight();
      case ENTER, SPACE -> shoot();
    }
  }

  public void addChildren(Node node) {
    synchronized (root) {
      root.getChildren().add(node);
    }
  }

  public void removeChild(Node node) {
    synchronized (root) {
      root.getChildren().remove(node);
    }
  }

  public void removeAllChildren(Collection<? extends Node> node) {
    synchronized (root) {
      root.getChildren().removeAll(node);
    }
  }

  public List<Sprite> getEnemies() {
    synchronized (root) {
      return getSpritesByCriteria(sprite -> sprite.getType().equals("enemy"));
    }
  }

  public void increaseHitsStat() {
    int actualHits = hits.incrementAndGet();
    Platform.runLater(() -> hitsStat.setText(String.format(HITS_LABEL_FORMAT, actualHits)));
    if (actualHits >= 30) {
      gameWin();
    }
  }

  private void gameWin() {
    finishGame("YOU WIN THE GAME!!!");
  }

  private void finishGame(String s) {
    synchronized (root) {
      enemyCreator.setSpawnStarted(false);
      scene.setOnKeyPressed(null);
      root.getChildren().clear();
      Text winText = new Text(s);
      winText.setTranslateX((double) GAME_WIDTH / 4);
      winText.setTranslateY((double) GAME_HEIGHT / 2);
      winText.setFont(Font.font(25));
      root.getChildren().add(winText);
    }
  }

  public void increaseMissesStat() {
    int actualMisses = misses.incrementAndGet();
    missesStat.setText(String.format(MISSES_LABEL_FORMAT, actualMisses));
    if (actualMisses >= 30) {
      gameLose();
    }
  }

  private void gameLose() {
    finishGame("YOU LOSE THE GAME!!!");
  }

  private List<Sprite> getSpritesByCriteria(Predicate<Sprite> spriteCriteria) {
    return getSpriteStream().filter(spriteCriteria).collect(Collectors.toList());
  }

  private Stream<Sprite> getSpriteStream() {
    return root.getChildren().stream().filter(Sprite.class::isInstance).map(Sprite.class::cast);
  }
}
