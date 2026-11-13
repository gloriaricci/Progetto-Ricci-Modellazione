package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.Optional;

public class ResourceBackgroundProvider implements BackgroundProvider {

    private static final String LEVEL_PATTERN = "/images/level%d.gif";
    private static final String DEFAULT_PATH = "/images/background.gif";

    @Override
    public Optional<Image> imageFor(int level) {
        return load(String.format(LEVEL_PATTERN, level))
                .or(() -> load(DEFAULT_PATH));
    }

    private Optional<Image> load(String path) {
        URL url = ResourceBackgroundProvider.class.getResource(path);
        if (url == null) {
            return Optional.empty();
        }
        return Optional.of(new Image(url.toExternalForm()));
    }
}