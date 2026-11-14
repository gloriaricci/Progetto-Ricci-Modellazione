package it.unicam.cs.mpgc.rpg129873.view.component;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.rpg129873.view.component.sprites.AnimationSpec;
import it.unicam.cs.mpgc.rpg129873.view.component.sprites.SpriteSpec;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpriteSpecTest {

    private static final AnimationSpec IDLE = new AnimationSpec(0, 6, 120, true);
    private static final AnimationSpec ATTACK = new AnimationSpec(3, 6, 80, false);

    @Test
    void validSpecExposesItsAnimations() {
        SpriteSpec spec = new SpriteSpec("nox.png", 69, 44,
                Map.of(SpriteSpec.IDLE, IDLE, "attack", ATTACK));

        assertEquals(IDLE, spec.animations().get("idle"));
        assertEquals(ATTACK, spec.animations().get("attack"));
    }

    @Test
    void aSpecWithoutIdleIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteSpec("nox.png", 69, 44, Map.of("attack", ATTACK)));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteSpec("nox.png", 69, 44, null));
    }

    @Test
    void idleMustLoop() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteSpec("nox.png", 69, 44, Map.of("idle", ATTACK)));
    }

    @Test
    void invalidFileOrSizeIsRejected() {
        Map<String, AnimationSpec> animations = Map.of("idle", IDLE);

        assertThrows(IllegalArgumentException.class, () -> new SpriteSpec(null, 69, 44, animations));
        assertThrows(IllegalArgumentException.class, () -> new SpriteSpec(" ", 69, 44, animations));
        assertThrows(IllegalArgumentException.class, () -> new SpriteSpec("a.png", 0, 44, animations));
        assertThrows(IllegalArgumentException.class, () -> new SpriteSpec("a.png", 69, 0, animations));
    }

    @Test
    void gsonReadsASpecWithAnimations() {
        String json = """
                { "file": "nox.png", "frameWidth": 69, "frameHeight": 44,
                  "animations": {
                    "idle":   { "row": 0, "frames": 6, "frameMillis": 120, "loop": true },
                    "attack": { "row": 3, "frames": 6, "frameMillis": 80,  "loop": false }
                  } }
                """;

        SpriteSpec spec = new Gson().fromJson(json, SpriteSpec.class);

        assertEquals(new SpriteSpec("nox.png", 69, 44,
                Map.of("idle", IDLE, "attack", ATTACK)), spec);
    }
}