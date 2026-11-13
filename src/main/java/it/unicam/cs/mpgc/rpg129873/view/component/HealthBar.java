package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar extends StackPane {

    private static final double BORDER = 3;
    private static final double LOW_THRESHOLD = 0.25;
    private static final double MEDIUM_THRESHOLD = 0.5;

    private final double innerWidth;
    private final Rectangle fill;
    private final Label text = new Label();

    public HealthBar(double width, double height) {
        this.innerWidth = width - 2 * BORDER;

        Rectangle background = new Rectangle(width, height, Color.web("#2b2b2b"));
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(BORDER);

        fill = new Rectangle(innerWidth, height - 2 * BORDER, Color.LIMEGREEN);
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);
        StackPane.setMargin(fill, new Insets(0, 0, 0, BORDER));

        text.setStyle("-fx-text-fill: white; -fx-font-size: 10px;");

        setMinSize(width, height);
        setMaxSize(width, height);
        getChildren().addAll(background, fill, text);
    }

    public void update(int current, int max) {
        double ratio = max > 0 ? Math.max(0, Math.min(1.0, (double) current / max)) : 0;
        fill.setWidth(innerWidth * ratio);
        fill.setFill(colorFor(ratio));
        text.setText(current + "/" + max);
    }

    private Color colorFor(double ratio) {
        if (ratio <= LOW_THRESHOLD) {
            return Color.CRIMSON;
        }
        if (ratio <= MEDIUM_THRESHOLD) {
            return Color.ORANGE;
        }
        return Color.LIMEGREEN;
    }
}
