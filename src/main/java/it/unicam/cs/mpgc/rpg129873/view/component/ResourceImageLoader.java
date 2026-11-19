package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Carica immagini dalle risorse e le tiene in cache, anche quando mancano o sono rovinate
 * (in quel caso restituisce {@code Optional.empty()}): un file assente non viene cercato di nuovo a ogni aggiornamento.
 */
public class ResourceImageLoader implements ImageLoader {

    private final Map<String, Optional<Image>> cache = new ConcurrentHashMap<>();

    @Override
    public Optional<Image> load(String resourcePath) {
        return cache.computeIfAbsent(resourcePath, this::read);
    }

    private Optional<Image> read(String resourcePath) {
        URL url = ResourceImageLoader.class.getResource(resourcePath);
        if (url == null) {
            return Optional.empty();
        }
        Image image = new Image(url.toExternalForm());
        return image.isError() ? Optional.empty() : Optional.of(image);
    }
}
