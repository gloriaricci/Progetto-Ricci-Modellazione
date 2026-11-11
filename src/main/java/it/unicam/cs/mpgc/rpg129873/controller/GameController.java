package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.Game;
import it.unicam.cs.mpgc.rpg129873.game.GameStatus;
import it.unicam.cs.mpgc.rpg129873.persistence.GameRepository;

import java.util.Objects;

public class GameController {

    private final Game game;
    private final GameRepository repository;

    public GameController(Game game, GameRepository repository) {
        this.game = Objects.requireNonNull(game);
        this.repository = Objects.requireNonNull(repository);
    }

    public void start() {
        game.start();
    }

    public void onAttack() {
        perform(game::playerAttack);
    }

    public void onPotion() {
        perform(game::playerUsePotion);
    }

    public void onAbility() {
        perform(game::playerUseActiveAbility);
    }

    public boolean isFinished() {
        return game.isFinished();
    }

    public GameStatus getStatus(){
        return game.status();
    }

    private void perform(Runnable action) {
        int levelBefore = game.getCurrentLevelNumber();
        action.run();
        if (!game.isFinished() && game.getCurrentLevelNumber() != levelBefore) {
            repository.save(game.snapshot());
        }
    }
}