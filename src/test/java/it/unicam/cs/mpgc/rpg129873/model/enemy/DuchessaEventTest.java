package it.unicam.cs.mpgc.rpg129873.model.enemy;

import it.unicam.cs.mpgc.rpg129873.model.Hero;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuchessaEventTest {

    @Test
    void duchessaReportsHerLifeStealEveryTwoAttacks() {
        DuchessaDiSangue duchessa = new DuchessaDiSangue();
        Hero hero = new Hero("h1", "Eroe", 500, 0);

        duchessa.attack(hero);
        assertTrue(duchessa.consumeEvent().isEmpty());

        duchessa.attack(hero);
        assertTrue(duchessa.consumeEvent().isPresent());
    }
}
