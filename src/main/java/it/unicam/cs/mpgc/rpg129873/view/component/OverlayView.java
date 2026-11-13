package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

public class OverlayView extends StackPane {

    private static final String BACKGROUND = "-fx-background-color: rgba(0, 0, 0, 0.78);";
    private static final String TITLE_STYLE = "-fx-font-size: 56px; -fx-font-weight: bold;";
    private static final String SUBTITLE_STYLE = "-fx-font-size: 28px;";
    private static final String HINT_STYLE = "-fx-font-size: 16px;";
    private static final double MAX_TEXT_WIDTH = 800;

    private final Label title = new Label();
    private final Label subtitle = new Label();
    private final Runnable onClosed;

    public OverlayView(Runnable onClosed) {
        this.onClosed = Objects.requireNonNull(onClosed);

        configure(title, TITLE_STYLE);
        configure(subtitle, SUBTITLE_STYLE);

        VBox texts = new VBox(16, title, subtitle);
        texts.setAlignment(Pos.CENTER);

        Label hint = new Label("Clicca per continuare");
        hint.setStyle(HINT_STYLE);
        hint.setTextFill(Color.LIGHTGRAY);
        StackPane.setAlignment(hint, Pos.BOTTOM_CENTER);
        StackPane.setMargin(hint, new Insets(0, 0, 32, 0));

        setStyle(BACKGROUND);
        getChildren().addAll(texts, hint);
        setVisible(false);
        setOnMouseClicked(e -> close());
    }

    public void show(String titleText, String subtitleText) {
        title.setText(titleText);

        boolean hasSubtitle = subtitleText != null && !subtitleText.isBlank();
        subtitle.setText(hasSubtitle ? subtitleText : "");
        subtitle.setVisible(hasSubtitle);
        subtitle.setManaged(hasSubtitle);

        setVisible(true);
        toFront();
    }

    public boolean isShowing() {
        return isVisible();
    }

    private void close() {
        setVisible(false);
        onClosed.run();
    }

    private void configure(Label label, String style) {
        label.setStyle(style);
        label.setTextFill(Color.WHITE);
        label.setWrapText(true);
        label.setMaxWidth(MAX_TEXT_WIDTH);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
    }
}
