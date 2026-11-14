package it.unicam.cs.mpgc.rpg129873.view.component;

import it.unicam.cs.mpgc.rpg129873.view.component.sprites.AnimationSpec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimationSpecTest {

    @Test
    void validValuesAreKept() {
        AnimationSpec spec = new AnimationSpec(3, 6, 80, false);

        assertEquals(3, spec.row());
        assertEquals(6, spec.frames());
        assertEquals(80, spec.frameMillis());
        assertFalse(spec.loop());
    }

    @Test
    void invalidValuesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new AnimationSpec(-1, 6, 80, true));
        assertThrows(IllegalArgumentException.class, () -> new AnimationSpec(0, 0, 80, true));
        assertThrows(IllegalArgumentException.class, () -> new AnimationSpec(0, 6, 0, true));
    }
}
