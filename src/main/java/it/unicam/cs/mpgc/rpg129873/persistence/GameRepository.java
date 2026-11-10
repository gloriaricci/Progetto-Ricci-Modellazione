package it.unicam.cs.mpgc.rpg129873.persistence;

import it.unicam.cs.mpgc.rpg129873.game.GameState;

import java.util.Optional;

public interface GameRepository {

    void save(GameState state);

    Optional<GameState> load();
}
