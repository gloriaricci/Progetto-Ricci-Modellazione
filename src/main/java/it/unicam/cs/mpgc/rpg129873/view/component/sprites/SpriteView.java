package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;

public class SpriteView extends StackPane {

    private static final double FALLBACK_SIZE = 160;
    private static final double FALLBACK_BORDER = 4;

    private final SpriteProvider provider;
    private final int defaultScale;
    private final Rectangle placeholder;
    private final ImageView imageView = new ImageView();
    private Timeline timeline;
    private SpriteSpec currentSpec;
    private FramePlayer player;
    private String currentId;

    public SpriteView(SpriteProvider provider, Color fallbackColor, int defaultScale) {
        if (defaultScale < 1) {
            throw new IllegalArgumentException("scale must be at least 1");
        }
        this.provider = Objects.requireNonNull(provider);
        this.defaultScale = defaultScale;

        placeholder = new Rectangle(FALLBACK_SIZE, FALLBACK_SIZE, Objects.requireNonNull(fallbackColor));
        placeholder.setStroke(Color.BLACK);
        placeholder.setStrokeWidth(FALLBACK_BORDER);

        imageView.setSmooth(false);
        imageView.setPreserveRatio(false);

        getChildren().addAll(placeholder, imageView);
        showPlaceholder();
    }

    public void setSprite(String id) {
        if (Objects.equals(id, currentId)) {
            return;
        }
        currentId = id;
        stopTimeline();
        currentSpec = null;
        player = null;
        provider.spriteFor(id).ifPresentOrElse(this::showSprite, this::showPlaceholder);
    }

    public void play(String animationName) {
        if (player != null && player.play(animationName)) {
            restartTimeline();
        }
    }

    private void showSprite(Sprite sprite) {
        currentSpec = sprite.spec();
        player = new FramePlayer(currentSpec.animations());
        int scale = currentSpec.scale() > 0 ? currentSpec.scale() : defaultScale;
        imageView.setImage(sprite.sheet());
        imageView.setFitWidth((double) currentSpec.frameWidth() * scale);
        imageView.setFitHeight((double) currentSpec.frameHeight() * scale);
        setSpriteVisible(true);
        restartTimeline();
    }

    private void showPlaceholder() {
        setSpriteVisible(false);
    }

    private void setSpriteVisible(boolean spriteVisible) {
        imageView.setVisible(spriteVisible);
        imageView.setManaged(spriteVisible);
        placeholder.setVisible(!spriteVisible);
        placeholder.setManaged(!spriteVisible);
    }

    private void restartTimeline() {
        stopTimeline();
        showFrame();
        if (player.needsTimer()) {
            timeline = new Timeline(new KeyFrame(Duration.millis(player.frameMillis()),
                    e -> nextFrame()));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    private void nextFrame() {
        if (isDetached()) {
            stopTimeline();
            return;
        }
        if (player.advance()) {
            restartTimeline();
        } else {
            showFrame();
        }
    }

    private void showFrame() {
        imageView.setViewport(new Rectangle2D(
                (double) player.column() * currentSpec.frameWidth(),
                (double) player.row() * currentSpec.frameHeight(),
                currentSpec.frameWidth(),
                currentSpec.frameHeight()));
    }

    // La vista è "staccata" quando la sua scena non è più mostrata da nessuna finestra
    private boolean isDetached() {
        Scene scene = getScene();
        return scene != null && scene.getWindow() == null;
    }

    private void stopTimeline() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }
}
