package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.view.component.BackgroundProvider;
import it.unicam.cs.mpgc.rpg129873.view.component.LevelBackground;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class MenuView {

    private static final int MENU_BACKGROUND_LEVEL = 1;

    private final StackPane root = new StackPane();

    public MenuView(boolean hasSave, BackgroundProvider backgrounds,
                    Runnable onPlay, Runnable onRestart) {
        Objects.requireNonNull(backgrounds);
        Objects.requireNonNull(onPlay);
        Objects.requireNonNull(onRestart);

        LevelBackground background = new LevelBackground(backgrounds);
        background.setLevel(MENU_BACKGROUND_LEVEL);

        Label title = new Label("Benvenuto nel gioco");
        title.getStyleClass().add("menu-title");

        Button playButton = new Button("Inizia a giocare");
        playButton.getStyleClass().add("pixel-button");
        playButton.setOnAction(e -> onPlay.run());

        Button restartButton = new Button("Ricomincia il gioco");
        restartButton.getStyleClass().add("pixel-button");
        restartButton.setDisable(!hasSave);
        restartButton.setOnAction(e -> onRestart.run());

        VBox content = new VBox(28, title, playButton, restartButton);
        content.setAlignment(Pos.CENTER);

        root.getChildren().addAll(background, content);
    }

    public Parent getRoot() {
        return root;
    }
}
