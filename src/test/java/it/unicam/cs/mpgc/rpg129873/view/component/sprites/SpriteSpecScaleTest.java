package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpriteSpecScaleTest {

    private static final Map<String, AnimationSpec> IDLE_ONLY =
            Map.of("idle", new AnimationSpec(0, 1, 100, true));

    @Test
    void scaleDefaultsToZeroMeaningTheViewDefault() {
        assertEquals(0, new SpriteSpec("a.png", 64, 64, IDLE_ONLY).scale());
    }

    @Test
    void scaleIsKept() {
        assertEquals(2, new SpriteSpec("a.png", 64, 64, IDLE_ONLY, 2).scale());
    }

    @Test
    void negativeScaleIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteSpec("a.png", 64, 64, IDLE_ONLY, -1));
    }

    @Test
    void gsonReadsTheScale() {
        String json = """
                { "file": "a.png", "frameWidth": 64, "frameHeight": 64, "scale": 2,
                  "animations": { "idle": { "row": 0, "frames": 1, "frameMillis": 100, "loop": true } } }
                """;

        assertEquals(new SpriteSpec("a.png", 64, 64, IDLE_ONLY, 2),
                new Gson().fromJson(json, SpriteSpec.class));
    }
}
