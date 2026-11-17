package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FramePlayerTest {

    private static final AnimationSpec IDLE = new AnimationSpec(0, 3, 100, true);
    private static final AnimationSpec ATTACK = new AnimationSpec(2, 2, 50, false);

    private FramePlayer player;

    @BeforeEach
    void setUp() {
        player = new FramePlayer(Map.of(SpriteSpec.IDLE, IDLE, "attack", ATTACK));
    }

    @Test
    void startsOnTheFirstIdleFrame() {
        assertEquals(0, player.row());
        assertEquals(0, player.column());
        assertEquals(100, player.frameMillis());
    }

    @Test
    void aLoopingAnimationWrapsAroundWithoutChangingAnimation() {
        assertFalse(player.advance());
        assertEquals(1, player.column());
        assertFalse(player.advance());
        assertEquals(2, player.column());

        assertFalse(player.advance());

        assertEquals(0, player.column());
        assertEquals(0, player.row());
    }

    @Test
    void aNonLoopingAnimationReturnsToIdleWhenFinished() {
        assertTrue(player.play("attack"));
        assertEquals(2, player.row());
        assertEquals(50, player.frameMillis());

        assertFalse(player.advance());
        assertEquals(1, player.column());

        assertTrue(player.advance());
        assertEquals(0, player.row());
        assertEquals(0, player.column());
        assertEquals(100, player.frameMillis());
    }

    @Test
    void startFrameShiftsTheColumn() {
        FramePlayer shifted = new FramePlayer(
                Map.of(SpriteSpec.IDLE, new AnimationSpec(2, 4, 100, true, 1)));

        assertEquals(1, shifted.column());
        shifted.advance();
        assertEquals(2, shifted.column());
    }

    @Test
    void anUnknownAnimationIsIgnored() {
        player.advance();

        assertFalse(player.play("dance"));

        assertEquals(1, player.column());
        assertEquals(0, player.row());
    }

    @Test
    void playingAnAnimationAgainRestartsItFromTheFirstFrame() {
        player.play("attack");
        player.advance();

        player.play("attack");

        assertEquals(0, player.column());
    }

    @Test
    void aTimerIsNeededOnlyWhenSomethingMustAdvance() {
        FramePlayer staticSprite = new FramePlayer(
                Map.of(SpriteSpec.IDLE, new AnimationSpec(0, 1, 1000, true),
                        "blink", new AnimationSpec(1, 1, 80, false)));

        assertFalse(staticSprite.needsTimer());
        assertTrue(player.needsTimer());

        staticSprite.play("blink");
        assertTrue(staticSprite.needsTimer());
    }

    @Test
    void theIdleAnimationIsRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new FramePlayer(Map.of("attack", ATTACK)));
        assertThrows(NullPointerException.class, () -> new FramePlayer(null));
    }
}

