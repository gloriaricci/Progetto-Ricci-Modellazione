package it.unicam.cs.mpgc.rpg129873.reward;

import it.unicam.cs.mpgc.rpg129873.ability.CuraDeiTemplari;
import it.unicam.cs.mpgc.rpg129873.ability.FuriaShimmer;
import it.unicam.cs.mpgc.rpg129873.ability.ScattoRapido;
import it.unicam.cs.mpgc.rpg129873.model.Hero;
import it.unicam.cs.mpgc.rpg129873.weapon.Pugnale;
import it.unicam.cs.mpgc.rpg129873.weapon.Spada;
import it.unicam.cs.mpgc.rpg129873.weapon.SpadaTemplari;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RewardTest {

    private Hero hero;

    @BeforeEach
    void setUp() {
        hero = new Hero("h1", "Eroe", 100, 0);
        hero.setWeapon(new Pugnale());
    }

    @Test
    void scattoRapidoGivesThePassive() {
        new RewardScattoRapido().apply(hero);
        assertInstanceOf(ScattoRapido.class, hero.getPassive());
    }

    @Test
    void spadaReplacesTheDagger() {
        new RewardSpada().apply(hero);
        assertInstanceOf(Spada.class, hero.getCurrentWeapon());
        assertEquals(22, hero.getAttackPower());
    }

    @Test
    void spadaTemplariReplacesWeaponAndPassive() {
        new RewardScattoRapido().apply(hero);
        new RewardSpada().apply(hero);
        new RewardSpadaTemplari().apply(hero);

        assertInstanceOf(SpadaTemplari.class, hero.getCurrentWeapon());
        assertInstanceOf(CuraDeiTemplari.class, hero.getPassive());
    }

    @Test
    void furiaShimmerGivesTheActiveAbility() {
        new RewardFuriaShimmer().apply(hero);
        assertTrue(hero.hasActiveAbility());
        assertInstanceOf(FuriaShimmer.class, hero.getActiveAbility());
    }

    @Test
    void noRewardChangesNothing() {
        new RewardNessuna().apply(hero);

        assertInstanceOf(Pugnale.class, hero.getCurrentWeapon());
        assertNull(hero.getPassive());
        assertFalse(hero.hasActiveAbility());
    }
}