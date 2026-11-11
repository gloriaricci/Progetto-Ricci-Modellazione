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
        beginPlayerTurn();
        Enemy enemy = getCurrentEnemy();
        int enemyHpBefore = enemy.getCurrentHp();
        player.attack(enemy);
        listener.onMessage(player.getName() + " attacca " + enemy.getName()
                + " e infligge " + (enemyHpBefore - enemy.getCurrentHp()) + " danni.");
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
        beginPlayerTurn();
        Enemy enemy = getCurrentEnemy();
        int enemyHpBefore = enemy.getCurrentHp();
        int heroHpBefore = player.getCurrentHp();
        player.useActiveAbility(enemy);
        listener.onMessage(player.getActiveAbility().getName() + ": infligge "
                + (enemyHpBefore - enemy.getCurrentHp()) + " danni a " + enemy.getName() + ".");
        int recoil = heroHpBefore - player.getCurrentHp();
        if (recoil > 0) {
            listener.onMessage("Il contraccolpo ti costa " + recoil + " punti vita.");
        }
        afterPlayerAction();
    }

    public void enemyTurn() {
        if (!enemyTurnPending) {
            return;
        }
        enemyTurnPending = false;
        Enemy enemy = getCurrentEnemy();
        int heroHpBefore = player.getCurrentHp();
        enemy.attack(player);
        listener.onMessage(enemy.getName() + " ti attacca e infligge "
                + (heroHpBefore - player.getCurrentHp()) + " danni.");
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

    private void beginPlayerTurn() {
        int hpBefore = player.getCurrentHp();
        player.onTurnStart();
        int healed = player.getCurrentHp() - hpBefore;
        if (healed > 0) {
            listener.onMessage("Recuperi " + healed + " punti vita.");
        }
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

        applyReward();

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

    private void applyReward() {
        Loadout before = Loadout.of(player);
        level.getReward().apply(player);
        Loadout.of(player).gainedSince(before).forEach(listener::onMessage);
    }

    private void applyProgression() {
        player.setMaxHp(progression.maxHpForLevel(currentLevelNumber));
        player.setCurrentHp(player.getMaxHp());
    }

    private void announceLevel() {
        listener.onMessage("Inizio Livello " + currentLevelNumber + ": " + getCurrentEnemy().getName());
    }
}