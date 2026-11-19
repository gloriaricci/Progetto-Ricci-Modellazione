package it.unicam.cs.mpgc.rpg129873.game;

/**
 * Canale attraverso cui {@link Game} comunica che cosa accade in partita
 * (attacchi, cure, cambi di livello...) senza sapere chi ascolta.
 * Una GUI, un log su console o un client web possono implementarla senza
 * toccare le regole di gioco. Viene chiamata in modo sincrono, durante
 * l'azione che ha generato l'evento: deve essere veloce e non dovrebbe
 * richiamare metodi di {@link Game} che modificano lo stato.
 */

public interface GameEventListener {
    void onEvent(GameEvent event);
}
