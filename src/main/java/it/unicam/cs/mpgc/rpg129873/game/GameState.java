package it.unicam.cs.mpgc.rpg129873.game;

/**
 * Dati minimi salvati su disco per riprendere una partita: livello, vita e pozioni.
 * Non contiene arma e abilità, che {@link GameFactory#restore} ricostruisce
 * rigiocando le ricompense dei livelli superati, né i contatori dei turni
 * (turni dell'eroe, usi di Furia Shimmer, attacchi dei nemici): dopo un
 * caricamento ripartono da zero. Il salvataggio è un checkpoint a inizio
 * livello, quindi il nemico riparte con la vita piena.
 * Il costruttore rifiuta valori non validi: così un file alterato viene scartato
 * già in lettura.
 */

public record GameState(int levelNumber, int currentHp, int potions) {

    public GameState {
        if (levelNumber < 1) {
            throw new IllegalArgumentException("Invalid level: " + levelNumber);
        }
        if (currentHp < 1) {
            throw new IllegalArgumentException("Invalid hp: " + currentHp);
        }
        if (potions < 0) {
            throw new IllegalArgumentException("Invalid potions: " + potions);
        }
    }
}
