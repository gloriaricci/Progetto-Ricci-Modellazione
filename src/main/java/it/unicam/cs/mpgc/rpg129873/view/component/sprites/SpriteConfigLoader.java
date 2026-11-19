package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * Legge {@code sprites.json} dalle risorse. Se il file manca o non è valido non blocca il
 * gioco: scrive un avviso su stderr e restituisce una mappa vuota, così la vista usa i riquadri di riserva.
 */
public class SpriteConfigLoader {

    private static final String DEFAULT_RESOURCE = "/images/sprites/sprites.json";
    private static final Type CONFIG_TYPE = new TypeToken<Map<String, SpriteSpec>>() {
    }.getType();

    private final String resource;

    public SpriteConfigLoader() {
        this(DEFAULT_RESOURCE);
    }

    public SpriteConfigLoader(String resource) {
        this.resource = Objects.requireNonNull(resource);
    }

    public Map<String, SpriteSpec> load() {
        try (InputStream in = SpriteConfigLoader.class.getResourceAsStream(resource)) {
            if (in == null) {
                warn("file non trovato");
                return Map.of();
            }
            Map<String, SpriteSpec> loaded =
                    new Gson().fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), CONFIG_TYPE);
            return loaded != null ? Map.copyOf(loaded) : Map.of();
        } catch (IOException | RuntimeException e) {
            warn("configurazione non valida (" + e.getMessage() + ")");
            return Map.of();
        }
    }

    private void warn(String message) {
        System.err.println("[sprites] " + resource + ": " + message
                + ". Verranno usati i riquadri di riserva.");
    }
}
