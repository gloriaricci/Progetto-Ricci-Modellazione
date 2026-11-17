package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.Game;
import it.unicam.cs.mpgc.rpg129873.persistence.GameRepository;

import java.util.Objects;

public class CheckpointManager {

    private final Game game;
    private final GameRepository repository;

    public CheckpointManager(Game game, GameRepository repository) {
        this.game = Objects.requireNonNull(game);
        this.repository = Objects.requireNonNull(repository);
    }

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
