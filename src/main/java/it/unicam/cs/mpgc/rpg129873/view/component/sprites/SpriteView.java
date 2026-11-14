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
    private final int scale;
    private final Rectangle placeholder;
    private final ImageView imageView = new ImageView();
    private Timeline timeline;
    private SpriteSpec currentSpec;
    private AnimationSpec currentAnimation;
    private String currentId;
    private int frameIndex;

    public SpriteView(SpriteProvider provider, Color fallbackColor, int scale) {
        if (scale < 1) {
            throw new IllegalArgumentException("scale must be at least 1");
        }
        this.provider = Objects.requireNonNull(provider);
        this.scale = scale;

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
        provider.spriteFor(id).ifPresentOrElse(this::showSprite, this::showPlaceholder);
    }

    public void play(String animationName) {
        if (currentSpec == null) {
            return;
        }
        AnimationSpec animation = currentSpec.animations().get(animationName);
        if (animation != null) {
            start(animation);
        }
    }

    private void showSprite(Sprite sprite) {
        currentSpec = sprite.spec();
        imageView.setImage(sprite.sheet());
        imageView.setFitWidth((double) currentSpec.frameWidth() * scale);
        imageView.setFitHeight((double) currentSpec.frameHeight() * scale);
        setSpriteVisible(true);
        start(currentSpec.animations().get(SpriteSpec.IDLE));
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

    private void start(AnimationSpec animation) {
        stopTimeline();
        currentAnimation = animation;
        frameIndex = 0;
        showFrame();
        if (animation.frames() > 1 || !animation.loop()) {
            timeline = new Timeline(new KeyFrame(Duration.millis(animation.frameMillis()),
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
        if (frameIndex + 1 < currentAnimation.frames()) {
            frameIndex++;
            showFrame();
        } else if (currentAnimation.loop()) {
            frameIndex = 0;
            showFrame();
        } else {
            start(currentSpec.animations().get(SpriteSpec.IDLE));
        }
    }

    private void showFrame() {
        imageView.setViewport(new Rectangle2D(
                (double) frameIndex * currentSpec.frameWidth(),
                (double) currentAnimation.row() * currentSpec.frameHeight(),
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
