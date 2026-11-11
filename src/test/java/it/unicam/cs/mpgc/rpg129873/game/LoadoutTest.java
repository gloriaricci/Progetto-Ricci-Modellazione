package it.unicam.cs.mpgc.rpg129873.game;

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
    void noChangeProducesNoMessages() {
        Loadout before = Loadout.of(hero);
        new RewardNessuna().apply(hero);

        assertTrue(Loadout.of(hero).gainedSince(before).isEmpty());
    }

    @Test
    void spadaTemplariAnnouncesWeaponAndPassive() {
        Loadout before = Loadout.of(hero);
        new RewardSpadaTemplari().apply(hero);

        List<String> messages = Loadout.of(hero).gainedSince(before);

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).contains("La Grande Spada dei Templari"));
        assertTrue(messages.get(1).contains("Cura dei Templari"));
    }

    @Test
    void furiaShimmerAnnouncesTheActiveAbility() {
        Loadout before = Loadout.of(hero);
        new RewardFuriaShimmer().apply(hero);

        List<String> messages = Loadout.of(hero).gainedSince(before);

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Furia Shimmer"));
    }
}
