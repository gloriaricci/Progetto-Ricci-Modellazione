package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.view.component.BackgroundProvider;
import it.unicam.cs.mpgc.rpg129873.view.component.LevelBackground;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

public class EndView {

    private static final double MAX_MESSAGE_WIDTH = 700;

    private final StackPane root = new StackPane();

    public EndView(String title, String message, String titleStyleClass,
                   int backgroundLevel, BackgroundProvider backgrounds,
                   Runnable onBackToMenu) {
        Objects.requireNonNull(backgrounds);
        Objects.requireNonNull(onBackToMenu);

        LevelBackground background = new LevelBackground(backgrounds);
        background.setLevel(backgroundLevel);

        Region dim = new Region();
        dim.getStyleClass().add("end-dim");
        dim.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("end-title", titleStyleClass);

        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("end-message");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(MAX_MESSAGE_WIDTH);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setAlignment(Pos.CENTER);

        Button menuButton = new Button("Torna al menu");
        menuButton.getStyleClass().add("pixel-button");
        menuButton.setOnAction(e -> onBackToMenu.run());

        VBox content = new VBox(32, titleLabel, messageLabel, menuButton);
        content.setAlignment(Pos.CENTER);

        root.getChildren().addAll(background, dim, content);
    }

    public Parent getRoot() {
        return root;
    }
}
