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
    private final HealthProgression progression;
    private final GameEventListener listener;
    private Level level;
    private int currentLevelNumber;
    private boolean enemyTurnPending = false;

    public <T extends Hero & PotionHolder> Game(T player,
                                                LevelProvider levelProvider,
                                                HealthProgression progression,
                                                int startingLevel,
                                                GameEventListener listener) {
        this.player = Objects.requireNonNull(player);
        this.potions = player;
        this.levelProvider = Objects.requireNonNull(levelProvider);
        this.progression = Objects.requireNonNull(progression);
        this.listener = Objects.requireNonNull(listener);
        this.currentLevelNumber = startingLevel;
        this.level = levelProvider.createLevel(startingLevel);
    }

    public void start() {
        announceLevel();
    }

    public void playerAttack() {
        if (!canPlayerAct()) {
            return;
        }
        player.onTurnStart();
        player.attack(getCurrentEnemy());
        afterPlayerAction();
    }

    public void playerUsePotion() {
        if (!canPlayerAct()) {
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
        afterPlayerAction();
    }

    public void playerUseActiveAbility() {
        if (!canPlayerAct()) {
            return;
        }
        if (!player.hasActiveAbility()) {
            listener.onMessage("Non hai ancora nessuna abilità attiva.");
            return;
        }
        player.onTurnStart();
        player.useActiveAbility(getCurrentEnemy());
        afterPlayerAction();
    }

    public void enemyTurn() {
        if (!enemyTurnPending) {
            return;
        }
        enemyTurnPending = false;
        Enemy enemy = getCurrentEnemy();
        enemy.attack(player);
        enemy.consumeEvent().ifPresent(listener::onMessage);
        checkGameState();
    }

    public boolean isEnemyTurnPending() {
        return enemyTurnPending;
    }

    public boolean isFinished() {
        return !player.isAlive() || !getCurrentEnemy().isAlive();
    }

    public boolean isWon() {
        return isFinished() && player.isAlive();
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

    public GameStatus status() {
        Enemy enemy = getCurrentEnemy();
        return new GameStatus(
                currentLevelNumber,
                player.getName(),
                player.getCurrentHp(),
                player.getMaxHp(),
                potions.getPotionsCount(),
                player.hasActiveAbility(),
                enemy.getId(),
                enemy.getName(),
                enemy.getCurrentHp(),
                enemy.getMaxHp(),
                isFinished(),
                isWon());
    }

    private boolean canPlayerAct() {
        return !isFinished() && !enemyTurnPending;
    }

    private void afterPlayerAction() {
        if (!player.isAlive() || !getCurrentEnemy().isAlive()) {
            checkGameState();
        } else {
            enemyTurnPending = true;
        }
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
            applyProgression();
            announceLevel();
        } else {
            listener.onMessage("COMPLIMENTI! Hai sconfitto il Barone dello Shimmer "
                    + "e liberato la città. Hai vinto il gioco!");
        }
    }

    private void applyProgression() {
        player.setMaxHp(progression.maxHpForLevel(currentLevelNumber));
        player.setCurrentHp(player.getMaxHp());
    }

    private void announceLevel() {
        listener.onMessage("Inizio Livello " + currentLevelNumber + ": " + getCurrentEnemy().getName());
    }
}