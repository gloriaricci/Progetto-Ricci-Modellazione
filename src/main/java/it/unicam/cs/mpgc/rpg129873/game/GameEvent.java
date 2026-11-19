package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.model.EnemyEffect;

/**
 * Tutto ciò che può accadere in una partita, descritto come dato immutabile.
 * {@link Game} li emette tramite {@link GameEventListener}; la vista li
 * trasforma in testo ({@code EventTextFormatter}) e in animazioni
 * ({@code EventAnimationMapper}) senza interrogare direttamente il modello.
 * <p>
 * L'interfaccia è {@code sealed} e gli switch che la usano non hanno ramo
 * {@code default}: aggiungere un evento fa fallire la compilazione in tutti i
 * punti da aggiornare, così nessun evento resta senza testo o animazione.
 */

public sealed interface GameEvent {

    enum RefusalReason {FULL_HP, NO_POTIONS}

    enum RewardKind {WEAPON, PASSIVE, ACTIVE}

    record LevelStarted(int level, String enemyName) implements GameEvent {
    }

    record EnemyArrived(String enemyName) implements GameEvent {
    }

    record EnemyDefeated(String enemyName) implements GameEvent {
    }

    record HeroAttacked(String heroName, String enemyName, int damage) implements GameEvent {
    }

    record AbilityUsed(String abilityName, String enemyName, int damage) implements GameEvent {
    }

    record RecoilTaken(int amount) implements GameEvent {
    }

    record HeroHealed(int amount) implements GameEvent {
    }

    record PotionUsed(int remaining) implements GameEvent {
    }

    record ActionRefused(RefusalReason reason) implements GameEvent {
    }

    record EnemyAttacked(String enemyName, int damage) implements GameEvent {
    }

    record EnemySpecial(String enemyName, EnemyEffect effect) implements GameEvent { }

    record RewardGained(RewardKind kind, String name) implements GameEvent {
    }

    record HeroDefeated() implements GameEvent {
    }

    record GameWon() implements GameEvent {
    }
}
