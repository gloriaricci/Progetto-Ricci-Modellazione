package it.unicam.cs.mpgc.rpg129873.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

public class EndView {

    private final VBox root = new VBox(32);

    public EndView(String title, String message, Color titleColor, Runnable onBackToMenu) {
        Objects.requireNonNull(onBackToMenu);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 36px;");
        titleLabel.setTextFill(titleColor);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(700);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setAlignment(Pos.CENTER);

        Button menuButton = new Button("Torna al menu");
        menuButton.setOnAction(e -> onBackToMenu.run());

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, messageLabel, menuButton);
    }

    public Parent getRoot() {
        return root;
    }
}
