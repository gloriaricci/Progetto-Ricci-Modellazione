package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.Game;
import it.unicam.cs.mpgc.rpg129873.persistence.GameRepository;

import java.util.Objects;

/**
 * Decide quando salvare e quando cancellare il salvataggio, in base a cosa è
 * successo durante un'azione di gioco. Tiene questa politica fuori da {@link Game}
 * (che non conosce la persistenza) e da {@link GameController}.
 */
public class CheckpointManager {

    private final Game game;
    private final GameRepository repository;

    public CheckpointManager(Game game, GameRepository repository) {
        this.game = Objects.requireNonNull(game);
        this.repository = Objects.requireNonNull(repository);
    }

    /**
     * Esegue l'azione e poi aggiorna il salvataggio:
     * <ul>
     * <li>partita vinta: cancella il salvataggio, non c'è più nulla da riprendere;</li>
     * <li>partita ancora in corso e livello cambiato: salva un checkpoint all'inizio
     * del nuovo livello;</li>
     * <li>negli altri casi non fa nulla. In particolare, dopo una sconfitta il checkpoint
     * dell'inizio livello resta, e "continua" riparte da lì.</li>
     * </ul>
     * Si salva solo ai cambi di livello, non a ogni mossa. Passare al secondo gemello
     * non cambia il numero del livello e quindi non salva.
     */
    public void track(Runnable action) {
        Objects.requireNonNull(action);
        int levelBefore = game.getCurrentLevelNumber();
        action.run();
        if (game.isWon()) {
            repository.clear();
        } else if (!game.isFinished() && game.getCurrentLevelNumber() != levelBefore) {
            repository.save(game.snapshot());
        }
    }
}
