package com.yurwar.thread.war.threadwar;

import javafx.application.Application;
import javafx.stage.Stage;

public class ThreadWarApp extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    ThreadWarContext threadWarContext = new ThreadWarContext(stage);
  }
}
