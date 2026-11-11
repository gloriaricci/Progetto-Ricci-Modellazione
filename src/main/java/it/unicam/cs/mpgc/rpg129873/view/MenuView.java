package it.unicam.cs.mpgc.rpg129873.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuView {

    private final VBox root = new VBox(20);

    public MenuView(boolean hasSave, Runnable onPlay, Runnable onRestart){
        Label title = new Label("Benvenuto nel gioco");
        title.setStyle("-fx-font-size: 32px;");

        Button playButton = new Button("Inizia a giocare");
        playButton.setOnAction(e -> onPlay.run());

        Button restartButton = new Button("Ricomincia il gioco");
        restartButton.setDisable(!hasSave);
        restartButton.setOnAction(e -> onRestart.run());

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, playButton, restartButton);
    }

    public Parent getRoot(){
        return root;
    }
}
