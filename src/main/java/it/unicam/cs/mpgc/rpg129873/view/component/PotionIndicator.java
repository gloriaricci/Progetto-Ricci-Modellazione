package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class PotionIndicator extends HBox {

    private static final double RADIUS = 16;
    private static final Color AVAILABLE = Color.web("#d6336c");
    private static final Color USED = Color.GRAY;

    private final List<Circle> circles = new ArrayList<>();

    public PotionIndicator(int total, Runnable onUse) {
        super(8);
        Objects.requireNonNull(onUse);
        setAlignment(Pos.CENTER);

        for (int i = 0; i < total; i++) {
            Circle circle = new Circle(RADIUS, USED);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(3);
            circles.add(circle);
        }
        getChildren().addAll(circles);
        setOnMouseClicked(e -> onUse.run());
    }

    public void update(int available, boolean enabled) {
        for (int i = 0; i < circles.size(); i++) {
            circles.get(i).setFill(i < available ? AVAILABLE : USED);
        }
        setDisable(!enabled || available == 0);
    }
}
