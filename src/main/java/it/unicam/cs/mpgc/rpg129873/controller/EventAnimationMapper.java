package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.controller.AnimationCue.Actor;
import it.unicam.cs.mpgc.rpg129873.game.GameEvent;
import it.unicam.cs.mpgc.rpg129873.game.GameEvent.*;

import java.util.Optional;

public class EventAnimationMapper {

    public static final String ATTACK = "attack";
    public static final String ABILITY = "ability";

    public Optional<AnimationCue> cueFor(GameEvent event) {
        return switch (event) {
            case HeroAttacked _ -> Optional.of(new AnimationCue(Actor.HERO, ATTACK));
            case AbilityUsed _ -> Optional.of(new AnimationCue(Actor.HERO, ABILITY));
            case EnemyAttacked _ -> Optional.of(new AnimationCue(Actor.ENEMY, ATTACK));
            case LevelStarted _, EnemyArrived _, EnemyDefeated _, RecoilTaken _,
                 HeroHealed _, PotionUsed _, ActionRefused _, EnemySpecial _,
                 RewardGained _, HeroDefeated _, GameWon _ -> Optional.empty();
        };
    }
}
