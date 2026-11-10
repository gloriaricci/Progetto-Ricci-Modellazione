package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.controller.GameEventListener;
import it.unicam.cs.mpgc.rpg129873.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129873.model.Nox;
import it.unicam.cs.mpgc.rpg129873.weapon.Pugnale;

import java.util.Objects;

public class GameFactory {

    private final LevelProvider levelProvider;
    private final GameEventListener listener;

    public GameFactory(LevelProvider levelProvider, GameEventListener listener) {
        this.levelProvider = Objects.requireNonNull(levelProvider);
        this.listener = Objects.requireNonNull(listener);
    }

    public Game newGame() {
        return new Game(createNox(), levelProvider, 1, listener);
    }

    public Game restore(GameState state) {
        Nox nox = createNox();
        for (int i = 1; i < state.levelNumber(); i++) {
            levelProvider.createLevel(i).getReward().apply(nox);
        }
        nox.setCurrentHp(state.currentHp());
        nox.setPotionsCount(state.potions());
        return new Game(nox, levelProvider, state.levelNumber(), listener);
    }

    private Nox createNox() {
        Nox nox = new Nox();
        nox.setWeapon(new Pugnale());
        return nox;
    }
}
