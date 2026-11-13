package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent.RewardGained;
import it.unicam.cs.mpgc.rpg129873.game.GameEvent.RewardKind;
import it.unicam.cs.mpgc.rpg129873.model.Hero;
import it.unicam.cs.mpgc.rpg129873.reward.RewardFuriaShimmer;
import it.unicam.cs.mpgc.rpg129873.reward.RewardNessuna;
import it.unicam.cs.mpgc.rpg129873.reward.RewardSpadaTemplari;
import it.unicam.cs.mpgc.rpg129873.weapon.Pugnale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadoutTest {

    private Hero hero;

    @BeforeEach
    void setUp() {
        hero = new Hero("h1", "Eroe", 100, 0);
        hero.setWeapon(new Pugnale());
    }

    @Test
    void noChangeProducesNoEvents() {
        Loadout before = Loadout.of(hero);
        new RewardNessuna().apply(hero);

        assertTrue(Loadout.of(hero).gainedSince(before).isEmpty());
    }

    @Test
    void spadaTemplariGainsWeaponAndPassive() {
        Loadout before = Loadout.of(hero);
        new RewardSpadaTemplari().apply(hero);

        List<RewardGained> gained = Loadout.of(hero).gainedSince(before);

        assertEquals(List.of(
                new RewardGained(RewardKind.WEAPON, "La Grande Spada dei Templari"),
                new RewardGained(RewardKind.PASSIVE, "Cura dei Templari")), gained);
    }

    @Test
    void furiaShimmerGainsTheActiveAbility() {
        Loadout before = Loadout.of(hero);
        new RewardFuriaShimmer().apply(hero);

        assertEquals(List.of(new RewardGained(RewardKind.ACTIVE, "Furia Shimmer")),
                Loadout.of(hero).gainedSince(before));
    }
}
