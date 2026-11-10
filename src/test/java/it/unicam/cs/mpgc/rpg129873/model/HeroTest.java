package it.unicam.cs.mpgc.rpg129873.model;

import it.unicam.cs.mpgc.rpg129873.ability.CuraDeiTemplari;
import it.unicam.cs.mpgc.rpg129873.ability.FuriaShimmer;
import it.unicam.cs.mpgc.rpg129873.ability.ScattoRapido;
import it.unicam.cs.mpgc.rpg129873.weapon.Pugnale;
import it.unicam.cs.mpgc.rpg129873.weapon.Spada;
import it.unicam.cs.mpgc.rpg129873.weapon.SpadaTemplari;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroTest {

    private Hero hero;
    private Enemy enemy;

    @BeforeEach
    void setUp() {
        hero = new Hero("h1", "Eroe", 100, 0);
        hero.setWeapon(new Pugnale());
        enemy = new Enemy("e1", "Nemico", 100, 10);
    }

    @Test
    void attackPowerIsTheWeaponDamage() {
        assertEquals(15, hero.getAttackPower());
    }

    @Test
    void newWeaponReplacesThePreviousOne() {
        hero.setWeapon(new Spada());
        assertEquals(22, hero.getAttackPower());
        hero.setWeapon(new SpadaTemplari());
        assertEquals(30, hero.getAttackPower());
    }

    @Test
    void attackDamagesTheEnemy() {
        hero.attack(enemy);
        assertEquals(85, enemy.getCurrentHp());
    }

    @Test
    void attackingNullDoesNothing() {
        assertDoesNotThrow(() -> hero.attack(null));
    }

    @Test
    void withoutActiveAbilityNothingHappens() {
        assertFalse(hero.hasActiveAbility());
        assertFalse(hero.useActiveAbility(enemy));
        assertEquals(100, enemy.getCurrentHp());
    }

    @Test
    void scattoRapidoAddsDamageEverySecondTurn() {
        hero.setPassive(new ScattoRapido());

        hero.onTurnStart();
        assertEquals(15, hero.getAttackPower());
        hero.onTurnStart();
        assertEquals(20, hero.getAttackPower());
        hero.onTurnStart();
        assertEquals(15, hero.getAttackPower());
        hero.onTurnStart();
        assertEquals(20, hero.getAttackPower());
    }

    @Test
    void newPassiveReplacesThePreviousOne() {
        hero.setPassive(new ScattoRapido());
        hero.setPassive(new CuraDeiTemplari());

        assertInstanceOf(CuraDeiTemplari.class, hero.getPassive());
        hero.onTurnStart();
        hero.onTurnStart();
        assertEquals(15, hero.getAttackPower());
    }

    @Test
    void curaDeiTemplariHealsEveryThirdTurn() {
        hero.setPassive(new CuraDeiTemplari());
        hero.setCurrentHp(50);

        hero.onTurnStart();
        hero.onTurnStart();
        assertEquals(50, hero.getCurrentHp());
        hero.onTurnStart();
        assertEquals(65, hero.getCurrentHp());
    }

    @Test
    void furiaShimmerDoublesTheDamage() {
        hero.setActiveAbility(new FuriaShimmer());

        assertTrue(hero.hasActiveAbility());
        assertTrue(hero.useActiveAbility(enemy));
        assertEquals(70, enemy.getCurrentHp());
    }

    @Test
    void furiaShimmerCostsHpEveryThirdUse() {
        Enemy dummy = new Enemy("big", "Bersaglio", 1000, 1);
        hero.setActiveAbility(new FuriaShimmer());

        hero.useActiveAbility(dummy);
        hero.useActiveAbility(dummy);
        assertEquals(100, hero.getCurrentHp());

        hero.useActiveAbility(dummy);
        assertEquals(95, hero.getCurrentHp());
        assertEquals(910, dummy.getCurrentHp());
    }
}
