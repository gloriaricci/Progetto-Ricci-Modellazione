package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import com.google.gson.Gson;
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
    void startFrameDefaultsToZero() {
        assertEquals(0, new AnimationSpec(3, 6, 80, false).startFrame());
    }

    @Test
    void startFrameIsKept() {
        assertEquals(1, new AnimationSpec(2, 12, 100, true, 1).startFrame());
    }

    @Test
    void gsonLeavesStartFrameAtZeroWhenMissing() {
        AnimationSpec spec = new Gson().fromJson(
                "{ \"row\": 0, \"frames\": 6, \"frameMillis\": 120, \"loop\": true }",
                AnimationSpec.class);

        assertEquals(new AnimationSpec(0, 6, 120, true), spec);
    }

    @Test
    void gsonReadsStartFrame() {
        AnimationSpec spec = new Gson().fromJson(
                "{ \"row\": 2, \"startFrame\": 1, \"frames\": 12, \"frameMillis\": 100, \"loop\": true }",
                AnimationSpec.class);

        assertEquals(new AnimationSpec(2, 12, 100, true, 1), spec);
    }

    @Test
    void invalidValuesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new AnimationSpec(-1, 6, 80, true));
        assertThrows(IllegalArgumentException.class, () -> new AnimationSpec(0, 0, 80, true));
        assertThrows(IllegalArgumentException.class, () -> new AnimationSpec(0, 6, 0, true));
        assertThrows(IllegalArgumentException.class, () -> new AnimationSpec(0, 6, 80, true, -1));
    }
}
