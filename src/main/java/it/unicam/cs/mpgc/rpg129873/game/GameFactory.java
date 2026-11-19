package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129873.model.Nox;
import it.unicam.cs.mpgc.rpg129873.weapon.Pugnale;

import java.util.Objects;

/**
 * Assembla le partite: unisce Nox, i livelli, la progressione della vita e il
 * listener. {@link #newGame()} parte dal livello 1, {@link #restore} riprende
 * da un {@link GameState}. È l'unico punto che sa come si costruisce una {@link Game}.
 */

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

    /**
     * Ricostruisce una partita da un salvataggio. Il salvataggio contiene solo
     * livello, vita e pozioni, quindi arma e abilità si recuperano rigiocando in
     * ordine le ricompense dei livelli già superati: ognuna sostituisce la precedente,
     * come nel gioco reale. Le ricompense vengono applicate direttamente a Nox,
     * quindi non generano eventi.
     *
     * @throws IllegalArgumentException se il livello salvato non esiste
     */

    public Game restore(GameState state) {
        if (state.levelNumber() > levelProvider.getTotalLevels()) {
            throw new IllegalArgumentException("Invalid level in save: " + state.levelNumber());
        }
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