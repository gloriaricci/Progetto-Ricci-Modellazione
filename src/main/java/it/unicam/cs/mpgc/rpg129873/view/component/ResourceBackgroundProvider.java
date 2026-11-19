package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.scene.image.Image;

import java.util.Objects;
import java.util.Optional;

/** Cerca {@code /images/level<N>.gif}; se manca usa {@code /images/background.gif}, e se manca anche quello non restituisce nulla. */
public class ResourceBackgroundProvider implements BackgroundProvider {

    private static final String LEVEL_PATTERN = "/images/level%d.gif";
    private static final String DEFAULT_PATH = "/images/background.gif";

    private final ImageLoader images;

    public ResourceBackgroundProvider(ImageLoader images) {
        this.images = Objects.requireNonNull(images);
    }

    @Override
    public Optional<Image> imageFor(int level) {
        return images.load(String.format(LEVEL_PATTERN, level))
                .or(() -> images.load(DEFAULT_PATH));
    }
}