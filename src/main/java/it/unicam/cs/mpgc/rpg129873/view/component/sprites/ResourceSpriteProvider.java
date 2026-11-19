package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import it.unicam.cs.mpgc.rpg129873.view.component.ImageLoader;
import javafx.scene.image.Image;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Trova lo sprite di un id. Se l'id è nella configurazione usa lo sheet animato descritto
 * lì, a patto che le animazioni stiano nell'immagine (altrimenti lo scarta con un avviso).
 * Se non è configurato, cerca {@code <id>.png} e lo mostra come immagine fissa a un solo frame.
 */
public class ResourceSpriteProvider implements SpriteProvider {

    private static final String FOLDER = "/images/sprites/";
    private static final int STATIC_FRAME_MILLIS = 1000;

    private final Map<String, SpriteSpec> specs;
    private final ImageLoader images;

    public ResourceSpriteProvider(Map<String, SpriteSpec> specs, ImageLoader images) {
        this.specs = Map.copyOf(Objects.requireNonNull(specs));
        this.images = Objects.requireNonNull(images);
    }

    @Override
    public Optional<Sprite> spriteFor(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        SpriteSpec configured = specs.get(id);
        return configured != null ? loadConfigured(configured) : loadStatic(id);
    }

    private Optional<Sprite> loadConfigured(SpriteSpec spec) {
        return images.load(FOLDER + spec.file())
                .filter(image -> fits(spec, image))
                .map(image -> new Sprite(image, spec));
    }

    private Optional<Sprite> loadStatic(String id) {
        String file = id + ".png";
        return images.load(FOLDER + file).map(image -> new Sprite(image,
                new SpriteSpec(file, (int) image.getWidth(), (int) image.getHeight(),
                        Map.of(SpriteSpec.IDLE, new AnimationSpec(0, 1, STATIC_FRAME_MILLIS, true)))));
    }

    private boolean fits(SpriteSpec spec, Image image) {
        boolean fits = spec.fitsIn((int) image.getWidth(), (int) image.getHeight());
        if (!fits) {
            System.err.println("[sprites] " + spec.file()
                    + ": le animazioni non stanno nell'immagine, sprite ignorato.");
        }
        return fits;
    }
}