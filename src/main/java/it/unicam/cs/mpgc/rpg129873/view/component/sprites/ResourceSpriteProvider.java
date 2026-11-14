package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class ResourceSpriteProvider implements SpriteProvider {

    private static final String FOLDER = "/images/sprites/";
    private static final String CONFIG = FOLDER + "sprites.json";
    private static final int STATIC_FRAME_MILLIS = 1000;
    private static final Type CONFIG_TYPE = new TypeToken<Map<String, SpriteSpec>>() { }.getType();

    private final Map<String, SpriteSpec> specs = loadSpecs();

    @Override
    public Optional<Sprite> spriteFor(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        SpriteSpec configured = specs.get(id);
        return configured != null ? loadConfigured(configured) : loadStatic(id);
    }

    private Optional<Sprite> loadConfigured(SpriteSpec spec) {
        return loadImage(spec.file())
                .filter(image -> fits(spec, image))
                .map(image -> new Sprite(image, spec));
    }

    private Optional<Sprite> loadStatic(String id) {
        String file = id + ".png";
        return loadImage(file).map(image -> new Sprite(image,
                new SpriteSpec(file, (int) image.getWidth(), (int) image.getHeight(),
                        Map.of(SpriteSpec.IDLE, new AnimationSpec(0, 1, STATIC_FRAME_MILLIS, true)))));
    }

    private boolean fits(SpriteSpec spec, Image image) {
        return spec.animations().values().stream().allMatch(animation ->
                animation.frames() * spec.frameWidth() <= image.getWidth()
                        && (animation.row() + 1) * spec.frameHeight() <= image.getHeight());
    }

    private Optional<Image> loadImage(String file) {
        URL url = ResourceSpriteProvider.class.getResource(FOLDER + file);
        if (url == null) {
            return Optional.empty();
        }
        Image image = new Image(url.toExternalForm());
        return image.isError() ? Optional.empty() : Optional.of(image);
    }

    private static Map<String, SpriteSpec> loadSpecs() {
        try (InputStream in = ResourceSpriteProvider.class.getResourceAsStream(CONFIG)) {
            if (in == null) {
                return Map.of();
            }
            Map<String, SpriteSpec> loaded =
                    new Gson().fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), CONFIG_TYPE);
            return loaded != null ? loaded : Map.of();
        } catch (IOException | RuntimeException e) {
            return Map.of();
        }
    }
}