package it.unicam.cs.mpgc.rpg129873.level;

import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.reward.Reward;

import java.util.List;

/**
 * Un livello: i nemici da affrontare uno dopo l'altro (più di uno per i gemelli) e la
 * ricompensa da assegnare quando sono stati sconfitti tutti. Tiene anche il punto a cui
 * è arrivato lo scontro, quindi ogni istanza è usabile per una sola partita:
 * per ricominciare serve un livello nuovo (vedi {@link LevelProvider}).
 * Non applica la ricompensa da solo, lo fa {@code Game}.
 */
public class Level {

    private final int number;
    private final List<Enemy> enemies;
    private final Reward reward;
    private int currentIndex = 0;

    public Level(int number, List<Enemy> enemies, Reward reward) {
        this.number = number;
        this.enemies = enemies;
        this.reward = reward;
    }

    public int getNumber() {
        return number;
    }

    public Reward getReward() {
        return reward;
    }

    public Enemy getCurrentEnemy() {
        return enemies.get(currentIndex);
    }

    public boolean hasNextEnemy() {
        return currentIndex < enemies.size() - 1;
    }

    /**
     * Passa al nemico successivo. Non controlla i limiti: chiamare solo dopo aver verificato
     * {@link #hasNextEnemy()}.
     */
    public void nextEnemy() {
        currentIndex++;
    }
}
