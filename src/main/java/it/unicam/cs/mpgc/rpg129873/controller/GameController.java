package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.Game;
import it.unicam.cs.mpgc.rpg129873.game.GameStatus;

import java.util.Objects;

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