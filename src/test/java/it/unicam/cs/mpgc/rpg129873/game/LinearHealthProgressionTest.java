package it.unicam.cs.mpgc.rpg129873.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LinearHealthProgressionTest {

    private final HealthProgression progression = new LinearHealthProgression();

    @Test
    void levelOneStartsWithOneHundredHp() {
        assertEquals(100, progression.maxHpForLevel(1));
    }

    @Test
    void everyLevelAddsFortyHp() {
        assertEquals(140, progression.maxHpForLevel(2));
        assertEquals(180, progression.maxHpForLevel(3));
        assertEquals(220, progression.maxHpForLevel(4));
        assertEquals(260, progression.maxHpForLevel(5));
    }

    @Test
    void invalidLevelsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> progression.maxHpForLevel(0));
        assertThrows(IllegalArgumentException.class, () -> progression.maxHpForLevel(-1));
    }
}
