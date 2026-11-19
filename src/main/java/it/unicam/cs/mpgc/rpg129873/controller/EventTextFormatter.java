package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent;
import it.unicam.cs.mpgc.rpg129873.game.GameEvent.*;
import it.unicam.cs.mpgc.rpg129873.model.EnemyEffect;

/**
 * Trasforma gli eventi di gioco nei testi mostrati al giocatore, e stabilisce quali
 * eventi meritano una schermata a tutto schermo (annunci) e quali restano solo nel log.
 * Tiene tutte le frasi in un posto solo: il dominio non contiene testo, e tradurre
 * il gioco significa modificare solo questa classe. Gli switch non hanno ramo
 * {@code default}, quindi un nuovo {@link GameEvent} va gestito qui per compilare.
 */
public class EventTextFormatter {

    public String format(GameEvent event) {
        return switch (event) {
            case LevelStarted e -> "Inizio Livello " + e.level() + ": " + e.enemyName();
            case EnemyArrived e -> "Arriva " + e.enemyName() + "!";
            case EnemyDefeated e -> "Hai sconfitto " + e.enemyName() + "!";
            case HeroAttacked e -> e.heroName() + " attacca " + e.enemyName()
                    + " e infligge " + e.damage() + " danni.";
            case AbilityUsed e -> e.abilityName() + ": infligge " + e.damage()
                    + " danni a " + e.enemyName() + ".";
            case RecoilTaken e -> "Il contraccolpo ti costa " + e.amount() + " punti vita.";
            case HeroHealed e -> "Recuperi " + e.amount() + " punti vita.";
            case PotionUsed e -> "Hai usato una pozione. Pozioni rimanenti: " + e.remaining();
            case ActionRefused e -> refusalText(e.reason());
            case EnemyAttacked e -> e.enemyName() + " ti attacca e infligge " + e.damage() + " danni.";
            case EnemySpecial e -> specialText(e.enemyName(), e.effect());
            case RewardGained e -> rewardText(e);
            case HeroDefeated _ -> "Sei stato sconfitto. Game over.";
            case GameWon _ -> "COMPLIMENTI! Hai sconfitto il Barone dello Shimmer "
                    + "e liberato la città. Hai vinto il gioco!";
        };
    }

    public boolean isAnnouncement(GameEvent event) {
        return switch (event) {
            case LevelStarted _, RewardGained _, EnemyArrived _ -> true;
            case EnemyDefeated _, HeroAttacked _, AbilityUsed _, RecoilTaken _,
                 HeroHealed _, PotionUsed _, ActionRefused _, EnemyAttacked _,
                 EnemySpecial _, HeroDefeated _, GameWon _ -> false;
        };
    }

    /**
     * Costruisce l'annuncio di un evento. Chiamare solo per gli eventi per cui
     * {@link #isAnnouncement} è vero: per gli altri lancia {@link IllegalArgumentException}.
     */
    public Announcement toAnnouncement(GameEvent event) {
        return switch (event) {
            case LevelStarted e -> new Announcement("Livello " + e.level(), e.enemyName());
            case RewardGained e -> new Announcement(rewardText(e), "");
            case EnemyArrived e -> new Announcement(format(e), "");
            case EnemyDefeated _, HeroAttacked _, AbilityUsed _, RecoilTaken _,
                 HeroHealed _, PotionUsed _, ActionRefused _, EnemyAttacked _,
                 EnemySpecial _, HeroDefeated _, GameWon _ ->
                    throw new IllegalArgumentException("Not an announcement: " + event);
        };
    }

    private String refusalText(RefusalReason reason) {
        return switch (reason) {
            case FULL_HP -> "La tua vita è già al massimo.";
            case NO_POTIONS -> "Non ci sono più pozioni.";
        };
    }

    private String rewardText(RewardGained reward) {
        return switch (reward.kind()) {
            case WEAPON -> "Hai ottenuto l'arma: " + reward.name() + "!";
            case PASSIVE -> "Hai ottenuto l'abilità passiva: " + reward.name() + "!";
            case ACTIVE -> "Hai ottenuto l'abilità attiva: " + reward.name() + "!";
        };
    }

    private String specialText(String enemyName, EnemyEffect effect) {
        return switch (effect.kind()) {
            case LIFE_STEAL -> "Ruba vita!! " + enemyName + " recupera: "
                    + effect.amount() + " punti vita.";
            case TIME_DISTORTION -> "Distorsione temporale!! " + enemyName + " ricarica "
                    + effect.amount() + " punti vita.";
        };
    }
}
