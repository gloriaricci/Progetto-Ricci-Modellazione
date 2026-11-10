package it.unicam.cs.mpgc.rpg129873.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoxTest {

    private Nox nox;

    @BeforeEach
    void setUp() {
        nox = new Nox();
    }

    @Test
    void startsWithTheExpectedStats() {
        assertEquals("Nox", nox.getName());
        assertEquals(100, nox.getMaxHp());
        assertEquals(2, nox.getPotionsCount());
    }

    @Test
    void isAHeroThatCarriesPotions() {
        assertInstanceOf(Hero.class, nox);
        assertInstanceOf(PotionHolder.class, nox);
    }

    @Test
    void potionHealsAndIsConsumed() {
        nox.takeDamage(60);

        assertTrue(nox.usePotion());
        assertEquals(80, nox.getCurrentHp());
        assertEquals(1, nox.getPotionsCount());
    }

    @Test
    void potionIsRefusedAtFullHp() {
        assertFalse(nox.usePotion());
        assertEquals(2, nox.getPotionsCount());
    }

    @Test
    void potionIsRefusedWhenNoneAreLeft() {
        nox.setPotionsCount(0);
        nox.takeDamage(10);

        assertFalse(nox.usePotion());
        assertEquals(90, nox.getCurrentHp());
    }

    @Test
    void potionsCountNeverGoesNegative() {
        nox.setPotionsCount(-3);
        assertEquals(0, nox.getPotionsCount());
    }
}
