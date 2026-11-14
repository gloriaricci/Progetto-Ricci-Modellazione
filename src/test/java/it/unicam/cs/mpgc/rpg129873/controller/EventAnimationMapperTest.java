package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.controller.AnimationCue.Actor;
import it.unicam.cs.mpgc.rpg129873.game.GameEvent.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EventAnimationMapperTest {

    private final EventAnimationMapper mapper = new EventAnimationMapper();

    @Test
    void heroAttackMakesTheHeroAttack() {
        assertEquals(Optional.of(new AnimationCue(Actor.HERO, "attack")),
                mapper.cueFor(new HeroAttacked("Nox", "Boss", 15)));
    }

    @Test
    void activeAbilityMakesTheHeroUseTheAbilityAnimation() {
        assertEquals(Optional.of(new AnimationCue(Actor.HERO, "ability")),
                mapper.cueFor(new AbilityUsed("Furia Shimmer", "Boss", 30)));
    }

    @Test
    void enemyAttackMakesTheEnemyAttack() {
        assertEquals(Optional.of(new AnimationCue(Actor.ENEMY, "attack")),
                mapper.cueFor(new EnemyAttacked("Boss", 15)));
    }

    @Test
    void otherEventsHaveNoAnimation() {
        assertTrue(mapper.cueFor(new PotionUsed(1)).isEmpty());
        assertTrue(mapper.cueFor(new EnemyDefeated("Boss")).isEmpty());
        assertTrue(mapper.cueFor(new LevelStarted(1, "Boss")).isEmpty());
        assertTrue(mapper.cueFor(new HeroDefeated()).isEmpty());
        assertTrue(mapper.cueFor(new GameWon()).isEmpty());
    }
}
