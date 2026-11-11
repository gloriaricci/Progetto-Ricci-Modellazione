package it.unicam.cs.mpgc.rpg129873.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NoxApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Nox");
        stage.setScene(new Scene(new Label("JavaFX funziona!"), 400, 200));
        stage.show();
    }
}
