package it.unicam.cs.mpgc.rpg129873.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    private Entity entity;

    @BeforeEach
    void setUp() {
        entity = new Entity("e1", "Test", 100, 10) { };
    }

    @Test
    void startsWithFullHp() {
        assertEquals(100, entity.getCurrentHp());
        assertTrue(entity.isAlive());
    }

    @Test
    void nullOrBlankIdIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Entity(null, "x", 10, 1) { });
        assertThrows(IllegalArgumentException.class, () -> new Entity("  ", "x", 10, 1) { });
    }

    @Test
    void nullNameFallsBackToDefault() {
        Entity unnamed = new Entity("id", null, 10, 1) { };
        assertEquals("Unknown entity", unnamed.getName());
    }

    @Test
    void takeDamageReducesHp() {
        entity.takeDamage(30);
        assertEquals(70, entity.getCurrentHp());
    }

    @Test
    void hpNeverGoesBelowZero() {
        entity.takeDamage(500);
        assertEquals(0, entity.getCurrentHp());
        assertFalse(entity.isAlive());
    }

    @Test
    void negativeDamageIsIgnored() {
        entity.takeDamage(-20);
        assertEquals(100, entity.getCurrentHp());
    }

    @Test
    void healRestoresHp() {
        entity.takeDamage(30);
        entity.heal(10);
        assertEquals(80, entity.getCurrentHp());
    }

    @Test
    void healNeverExceedsMaxHp() {
        entity.takeDamage(30);
        entity.heal(100);
        assertEquals(100, entity.getCurrentHp());
    }

    @Test
    void setCurrentHpIsClamped() {
        entity.setCurrentHp(500);
        assertEquals(100, entity.getCurrentHp());
        entity.setCurrentHp(-5);
        assertEquals(0, entity.getCurrentHp());
    }

    @Test
    void loweringMaxHpCapsCurrentHp() {
        entity.setMaxHp(50);
        assertEquals(50, entity.getCurrentHp());
    }
}