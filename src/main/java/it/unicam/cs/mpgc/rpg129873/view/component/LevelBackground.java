package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Objects;

public class LevelBackground extends StackPane {

    private static final Color[] FALLBACK_COLORS = {
            Color.web("#2b2b3a"),
            Color.web("#3a2b22"),
            Color.web("#2b1f3a"),
            Color.web("#3a1f24"),
            Color.web("#1a1a24")
    };

    private final BackgroundProvider provider;
    private final ImageView imageView = new ImageView();
    private int currentLevel = -1;

    public LevelBackground(BackgroundProvider provider) {
        this.provider = Objects.requireNonNull(provider);

        imageView.setManaged(false);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(false);
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());

        getChildren().add(imageView);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setMouseTransparent(true);
    }

    public void setLevel(int level) {
        if (level == currentLevel) {
            return;
        }
        currentLevel = level;

        Image image = provider.imageFor(level).orElse(null);
        imageView.setImage(image);
        setBackground(new Background(new BackgroundFill(fallbackColor(level), null, null)));
    }

    private Color fallbackColor(int level) {
        return FALLBACK_COLORS[Math.floorMod(level - 1, FALLBACK_COLORS.length)];
    }
}
