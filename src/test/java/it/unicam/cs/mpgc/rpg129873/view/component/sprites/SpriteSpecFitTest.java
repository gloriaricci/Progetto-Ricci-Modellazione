package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpriteSpecFitTest {

    private static final SpriteSpec SPEC = new SpriteSpec("a.png", 64, 64, Map.of(
            "idle", new AnimationSpec(0, 6, 100, true),
            "attack", new AnimationSpec(1, 4, 80, false, 2)));

    @Test
    void anImageLargeEnoughFits() {
        assertTrue(SPEC.fitsIn(384, 128));
    }

    @Test
    void aTooNarrowImageDoesNotFit() {
        assertFalse(SPEC.fitsIn(383, 128));
    }

    @Test
    void aTooShortImageDoesNotFit() {
        assertFalse(SPEC.fitsIn(384, 127));
    }

    @Test
    void everyAnimationMustFitNotOnlyIdle() {
        assertFalse(SPEC.fitsIn(300, 128));
    }
}
