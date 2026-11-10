package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.controller.GameEventListener;
import it.unicam.cs.mpgc.rpg129873.level.Level;
import it.unicam.cs.mpgc.rpg129873.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.model.Hero;
import it.unicam.cs.mpgc.rpg129873.model.PotionHolder;

import java.util.Objects;

public class Game {

    private final Hero player;
    private final PotionHolder potions;
    private final LevelProvider levelProvider;
    private final GameEventListener listener;
    private Level level;
    private int currentLevelNumber;

    public <T extends Hero & PotionHolder> Game(T player,
                                                LevelProvider levelProvider,
                                                int startingLevel,
                                                GameEventListener listener) {
        this.player = Objects.requireNonNull(player);
        this.potions = player;
        this.levelProvider = Objects.requireNonNull(levelProvider);
        this.listener = Objects.requireNonNull(listener);
        this.currentLevelNumber = startingLevel;
        this.level = levelProvider.createLevel(startingLevel);
    }

    public void start() {
        announceLevel();
    }

    public void playerAttack() {
        if (isFinished()) {
            return;
        }
        player.onTurnStart();
        player.attack(getCurrentEnemy());
        endTurn();
    }

    public void playerUsePotion() {
        if (isFinished()) {
            return;
        }
        if (player.getCurrentHp() == player.getMaxHp()) {
            listener.onMessage("La tua vita è già al massimo.");
            return;
        }
        if (!potions.usePotion()) {
            listener.onMessage("Non hai più pozioni di cura.");
            return;
        }
        listener.onMessage("Hai usato una pozione. Pozioni rimanenti: " + potions.getPotionsCount());
        endTurn();
    }

    public void playerUseActiveAbility() {
        if (isFinished()) {
            return;
        }
        if (!player.hasActiveAbility()) {
            listener.onMessage("Non hai ancora nessuna abilità attiva.");
            return;
        }
        player.onTurnStart();
        player.useActiveAbility(getCurrentEnemy());
        endTurn();
    }

    public boolean isFinished() {
        return !player.isAlive() || !getCurrentEnemy().isAlive();
    }

    public Enemy getCurrentEnemy() {
        return level.getCurrentEnemy();
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    public GameState snapshot() {
        return new GameState(currentLevelNumber, player.getCurrentHp(), potions.getPotionsCount());
    }

    private void endTurn() {
        Enemy enemy = getCurrentEnemy();
        if (enemy.isAlive()) {
            enemy.attack(player);
            enemy.consumeEvent().ifPresent(listener::onMessage);
        }
        checkGameState();
    }

    private void checkGameState() {
        if (!player.isAlive()) {
            listener.onMessage("Sei stato sconfitto. Game over.");
            return;
        }
        if (getCurrentEnemy().isAlive()) {
            return;
        }

        listener.onMessage("Hai sconfitto " + getCurrentEnemy().getName() + "!");

        if (level.hasNextEnemy()) {
            level.nextEnemy();
            listener.onMessage("Arriva " + getCurrentEnemy().getName() + "!");
            return;
        }

        level.getReward().apply(player);

        if (currentLevelNumber < levelProvider.getTotalLevels()) {
            currentLevelNumber++;
            level = levelProvider.createLevel(currentLevelNumber);
            announceLevel();
        } else {
            listener.onMessage("COMPLIMENTI! Hai sconfitto il Barone dello Shimmer "
                    + "e liberato la città. Hai vinto il gioco!");
        }
    }

    private void announceLevel() {
        listener.onMessage("Inizio Livello " + currentLevelNumber + ": " + getCurrentEnemy().getName());
    }
}