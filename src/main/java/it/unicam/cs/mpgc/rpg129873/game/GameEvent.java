package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.model.EnemyEffect;

public sealed interface GameEvent {

    enum RefusalReason { FULL_HP, NO_POTIONS }

    enum RewardKind { WEAPON, PASSIVE, ACTIVE }

    record LevelStarted(int level, String enemyName) implements GameEvent { }

    record EnemyArrived(String enemyName) implements GameEvent { }

    record EnemyDefeated(String enemyName) implements GameEvent { }

    record HeroAttacked(String heroName, String enemyName, int damage) implements GameEvent { }

    record AbilityUsed(String abilityName, String enemyName, int damage) implements GameEvent { }

    record RecoilTaken(int amount) implements GameEvent { }

    record HeroHealed(int amount) implements GameEvent { }

    record PotionUsed(int remaining) implements GameEvent { }

    record ActionRefused(RefusalReason reason) implements GameEvent { }

    record EnemyAttacked(String enemyName, int damage) implements GameEvent { }

    record EnemySpecial(EnemyEffect effect) implements GameEvent { }

    record RewardGained(RewardKind kind, String name) implements GameEvent { }

    record HeroDefeated() implements GameEvent { }

    record GameWon() implements GameEvent { }
}
