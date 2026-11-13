package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129873.model.Nox;
import it.unicam.cs.mpgc.rpg129873.weapon.Pugnale;

import java.util.Objects;

public class GameFactory {

    private final LevelProvider levelProvider;
    private final HealthProgression progression;
    private final GameEventListener listener;

    public GameFactory(LevelProvider levelProvider, GameEventListener listener) {
        this(levelProvider, new LinearHealthProgression(), listener);
    }

    public GameFactory(LevelProvider levelProvider,
                       HealthProgression progression,
                       GameEventListener listener) {
        this.levelProvider = Objects.requireNonNull(levelProvider);
        this.progression = Objects.requireNonNull(progression);
        this.listener = Objects.requireNonNull(listener);
    }

    public Game newGame() {
        return new Game(createNox(1), levelProvider, progression, 1, listener);
    }

    public Game restore(GameState state) {
        Nox nox = createNox(state.levelNumber());
        for (int i = 1; i < state.levelNumber(); i++) {
            levelProvider.createLevel(i).getReward().apply(nox);
        }
        nox.setCurrentHp(state.currentHp());
        nox.setPotionsCount(state.potions());
        return new Game(nox, levelProvider, progression, state.levelNumber(), listener);
    }

    private Nox createNox(int levelNumber) {
        Nox nox = new Nox();
        nox.setMaxHp(progression.maxHpForLevel(levelNumber));
        nox.setCurrentHp(nox.getMaxHp());
        nox.setWeapon(new Pugnale());
        return nox;
    }
}