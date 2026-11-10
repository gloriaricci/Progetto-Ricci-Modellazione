package it.unicam.cs.mpgc.rpg129873.model.enemy;

import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.model.Hero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BossBehaviourTest {

    private Hero hero;

    @BeforeEach
    void setUp() {
        hero = new Hero("h1", "Eroe", 500, 0);
    }

    @Test
    void bossStatsMatchTheGameDesign() {
        assertStats(new CollezionistaDiOssa(), 100, 15);
        assertStats(new MadreBrassmonkey(), 140, 20);
        assertStats(new IGemelli("g1", "Gemello 1"), 100, 17);
        assertStats(new DuchessaDiSangue(), 220, 30);
        assertStats(new BaroneShimmer(), 300, 35);
    }

    @Test
    void aBossAttackDamagesTheHero() {
        new CollezionistaDiOssa().attack(hero);
        assertEquals(485, hero.getCurrentHp());

        new MadreBrassmonkey().attack(hero);
        assertEquals(465, hero.getCurrentHp());
    }

    @Test
    void baroneHealsEveryTwoAttacksAndReportsAnEvent() {
        BaroneShimmer barone = new BaroneShimmer();
        barone.takeDamage(100);

        barone.attack(hero);
        assertEquals(200, barone.getCurrentHp());
        assertTrue(barone.consumeEvent().isEmpty());

        barone.attack(hero);
        assertEquals(220, barone.getCurrentHp());
        assertTrue(barone.consumeEvent().isPresent());
        assertTrue(barone.consumeEvent().isEmpty());
        assertEquals(430, hero.getCurrentHp());
    }

    @Test
    void duchessaStealsLifeEveryTwoAttacks() {
        DuchessaDiSangue duchessa = new DuchessaDiSangue();
        duchessa.takeDamage(50);

        duchessa.attack(hero);
        assertEquals(170, duchessa.getCurrentHp());

        duchessa.attack(hero);
        assertEquals(180, duchessa.getCurrentHp());
        assertEquals(440, hero.getCurrentHp());
    }

    private void assertStats(Enemy enemy, int hp, int attack) {
        assertEquals(hp, enemy.getMaxHp());
        assertEquals(attack, enemy.getAttackPower());
    }
}