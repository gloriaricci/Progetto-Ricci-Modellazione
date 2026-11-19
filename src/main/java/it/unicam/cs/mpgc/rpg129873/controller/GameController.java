package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.Game;
import it.unicam.cs.mpgc.rpg129873.game.GameStatus;

import java.util.Objects;

/**
 * Unico punto di contatto tra la vista e le regole di gioco: traduce i comandi
 * dell'utente (attacco, pozione, abilità, risposta del nemico) in chiamate a {@link Game}
 * e fa passare ogni azione da {@link CheckpointManager}, così il salvataggio avviene senza
 * che la vista se ne occupi. Non dipende da JavaFX: un'altra interfaccia (mobile, web)
 * può riusarlo così com'è.
 */
public class GameController {

    private final Game game;
    private final CheckpointManager checkpoints;

    public GameController(Game game, CheckpointManager checkpoints) {
        this.game = Objects.requireNonNull(game);
        this.checkpoints = Objects.requireNonNull(checkpoints);
    }

    public void start() {
        game.start();
    }

    public void onAttack() {
        checkpoints.track(game::playerAttack);
    }

    public void onPotion() {
        checkpoints.track(game::playerUsePotion);
    }

    public void onAbility() {
        checkpoints.track(game::playerUseActiveAbility);
    }

    public void onEnemyTurn() {
        checkpoints.track(game::enemyTurn);
    }

    public boolean isEnemyTurnPending() {
        return game.isEnemyTurnPending();
    }

    public boolean isFinished() {
        return game.isFinished();
    }

    public GameStatus getStatus() {
        return game.status();
    }
}