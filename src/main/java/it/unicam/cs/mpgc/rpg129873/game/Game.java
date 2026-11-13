package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent.*;
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
        listener.onEvent(new HeroAttacked(player.getName(), enemy.getName(),
                enemyHpBefore - enemy.getCurrentHp()));
        afterPlayerAction();
    }

    public void playerUsePotion() {
        if (!canPlayerAct()) {
            return;
        }
        if (player.getCurrentHp() == player.getMaxHp()) {
            listener.onEvent(new ActionRefused(RefusalReason.FULL_HP));
            return;
        }
        if (!potions.usePotion()) {
            listener.onEvent(new ActionRefused(RefusalReason.NO_POTIONS));
            return;
        }
        listener.onEvent(new PotionUsed(potions.getPotionsCount()));
        afterPlayerAction();
    }

    public void playerUseActiveAbility() {
        if (!canPlayerAct()) {
            return;
        }
        if (!player.hasActiveAbility()) {
            listener.onEvent(new ActionRefused(RefusalReason.NO_ABILITY));
            return;
        }
        beginPlayerTurn();
        Enemy enemy = getCurrentEnemy();
        int enemyHpBefore = enemy.getCurrentHp();
        int heroHpBefore = player.getCurrentHp();
        player.useActiveAbility(enemy);
        listener.onEvent(new AbilityUsed(player.getActiveAbility().getName(), enemy.getName(),
                enemyHpBefore - enemy.getCurrentHp()));
        int recoil = heroHpBefore - player.getCurrentHp();
        if (recoil > 0) {
            listener.onEvent(new RecoilTaken(recoil));
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
        listener.onEvent(new EnemyAttacked(enemy.getName(), heroHpBefore - player.getCurrentHp()));
        enemy.consumeEvent().ifPresent(message -> listener.onEvent(new EnemySpecial(message)));
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
            listener.onEvent(new HeroHealed(healed));
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
            listener.onEvent(new HeroDefeated());
            return;
        }
        if (getCurrentEnemy().isAlive()) {
            return;
        }

        listener.onEvent(new EnemyDefeated(getCurrentEnemy().getName()));

        if (level.hasNextEnemy()) {
            level.nextEnemy();
            listener.onEvent(new EnemyArrived(getCurrentEnemy().getName()));
            return;
        }

        applyReward();

        if (currentLevelNumber < levelProvider.getTotalLevels()) {
            currentLevelNumber++;
            level = levelProvider.createLevel(currentLevelNumber);
            applyProgression();
            announceLevel();
        } else {
            listener.onEvent(new GameWon());
        }
    }

    private void applyReward() {
        Loadout before = Loadout.of(player);
        level.getReward().apply(player);
        Loadout.of(player).gainedSince(before).forEach(listener::onEvent);
    }

    private void applyProgression() {
        player.setMaxHp(progression.maxHpForLevel(currentLevelNumber));
        player.setCurrentHp(player.getMaxHp());
    }

    private void announceLevel() {
        listener.onEvent(new LevelStarted(currentLevelNumber, getCurrentEnemy().getName()));
    }
}