package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventTextFormatterTest {

    private final EventTextFormatter formatter = new EventTextFormatter();

    @Test
    void attackIsDescribedWithNamesAndDamage() {
        assertEquals("Nox attacca Goblin e infligge 15 danni.",
                formatter.format(new HeroAttacked("Nox", "Goblin", 15)));
    }

    @Test
    void rewardsAreDescribedByKind() {
        assertEquals("Hai ottenuto l'arma: Spada!",
                formatter.format(new RewardGained(RewardKind.WEAPON, "Spada")));
        assertEquals("Hai ottenuto l'abilità attiva: Furia Shimmer!",
                formatter.format(new RewardGained(RewardKind.ACTIVE, "Furia Shimmer")));
    }

    @Test
    void refusalsHaveTheirOwnText() {
        assertEquals("La tua vita è già al massimo.",
                formatter.format(new ActionRefused(RefusalReason.FULL_HP)));
        assertEquals("Non hai più pozioni di cura.",
                formatter.format(new ActionRefused(RefusalReason.NO_POTIONS)));
    }
}
