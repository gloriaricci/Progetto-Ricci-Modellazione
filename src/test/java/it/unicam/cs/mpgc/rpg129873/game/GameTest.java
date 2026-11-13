package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent.*;
import it.unicam.cs.mpgc.rpg129873.level.Level;
import it.unicam.cs.mpgc.rpg129873.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private final List<GameEvent> events = new ArrayList<>();
    private final AtomicBoolean rewardApplied = new AtomicBoolean(false);

    private Game newGame(List<Supplier<Level>> levels) {
        LevelProvider provider = new FakeLevelProvider(levels);
        return new GameFactory(provider, events::add).newGame();
    }

    private Game singleEnemyGame(int enemyHp, int enemyAttack) {
        return newGame(List.of(
                () -> new Level(1, List.of(new Enemy("e1", "Mostro", enemyHp, enemyAttack)), hero -> { })));
    }

    private boolean happened(GameEvent event) {
        return events.contains(event);
    }

    @Test
    void startAnnouncesTheFirstLevel() {
        Game game = singleEnemyGame(60, 5);
        game.start();
        assertTrue(happened(new LevelStarted(1, "Mostro")));
    }

    @Test
    void snapshotReflectsLevelHpAndPotions() {
        Game game = singleEnemyGame(60, 5);
        assertEquals(new GameState(1, 100, 2), game.snapshot());
    }

    @Test
    void attackDamagesTheEnemyThenTheEnemyHitsBack() {
        Game game = singleEnemyGame(60, 5);

        game.playerAttack();

        assertEquals(45, game.getCurrentEnemy().getCurrentHp());
        assertEquals(100, game.snapshot().currentHp());
        assertTrue(game.isEnemyTurnPending());

        game.enemyTurn();

        assertEquals(95, game.snapshot().currentHp());
        assertFalse(game.isEnemyTurnPending());
    }

    @Test
    void attackReportsTheDamageDealt() {
        Game game = singleEnemyGame(60, 5);

        game.playerAttack();

        assertTrue(happened(new HeroAttacked("Nox", "Mostro", 15)));
    }

    @Test
    void enemyReplyReportsTheDamageTaken() {
        Game game = singleEnemyGame(60, 5);

        game.playerAttack();
        game.enemyTurn();

        assertTrue(happened(new EnemyAttacked("Mostro", 5)));
    }

    @Test
    void playerCannotActAgainBeforeTheEnemyReplies() {
        Game game = singleEnemyGame(1000, 5);

        game.playerAttack();
        game.playerAttack();

        assertEquals(985, game.getCurrentEnemy().getCurrentHp());

        game.enemyTurn();
        game.playerAttack();

        assertEquals(970, game.getCurrentEnemy().getCurrentHp());
    }

    @Test
    void enemyTurnWithoutAPendingReplyDoesNothing() {
        Game game = singleEnemyGame(60, 5);

        game.enemyTurn();

        assertEquals(100, game.snapshot().currentHp());
    }

    @Test
    void aDefeatedEnemyDoesNotReply() {
        Game game = singleEnemyGame(15, 5);

        game.playerAttack();
        game.enemyTurn();

        assertFalse(game.isEnemyTurnPending());
        assertEquals(100, game.snapshot().currentHp());
    }

    @Test
    void twinsAreFoughtOneAfterTheOtherThenTheLevelAdvances() {
        Game game = newGame(List.of(
                () -> new Level(1,
                        List.of(new Enemy("a", "Goblin A", 15, 5), new Enemy("b", "Goblin B", 15, 5)),
                        hero -> rewardApplied.set(true)),
                () -> new Level(2, List.of(new Enemy("c", "Orco", 30, 5)), hero -> { })));

        game.playerAttack();
        assertEquals("Goblin B", game.getCurrentEnemy().getName());
        assertEquals(1, game.getCurrentLevelNumber());
        assertFalse(rewardApplied.get());
        assertEquals(100, game.snapshot().currentHp());

        game.playerAttack();
        assertTrue(rewardApplied.get());
        assertEquals(2, game.getCurrentLevelNumber());
        assertEquals("Orco", game.getCurrentEnemy().getName());
        assertFalse(game.isFinished());
    }

    @Test
    void changingLevelRaisesMaxHpAndRestoresHp() {
        Game game = newGame(List.of(
                () -> new Level(1, List.of(new Enemy("a", "Goblin", 30, 20)), hero -> { }),
                () -> new Level(2, List.of(new Enemy("b", "Orco", 1000, 5)), hero -> { })));

        game.playerAttack();
        game.enemyTurn();
        assertEquals(80, game.status().heroHp());

        game.playerAttack();

        assertEquals(2, game.getCurrentLevelNumber());
        assertEquals(140, game.status().heroMaxHp());
        assertEquals(140, game.status().heroHp());
    }

    @Test
    void defeatingTheLastBossWinsTheGame() {
        Game game = singleEnemyGame(30, 5);

        game.playerAttack();
        game.enemyTurn();
        assertFalse(game.isFinished());
        game.playerAttack();

        assertTrue(game.isFinished());
        assertTrue(happened(new GameWon()));
    }

    @Test
    void nothingHappensAfterTheGameIsFinished() {
        Game game = singleEnemyGame(15, 5);
        game.playerAttack();
        int eventsAtTheEnd = events.size();

        game.playerAttack();
        game.playerUsePotion();
        game.playerUseActiveAbility();
        game.enemyTurn();

        assertEquals(eventsAtTheEnd, events.size());
    }

    @Test
    void beingDefeatedEndsTheGame() {
        Game game = singleEnemyGame(1000, 100);

        game.playerAttack();
        game.enemyTurn();

        assertTrue(game.isFinished());
        assertTrue(happened(new HeroDefeated()));
    }

    @Test
    void potionAtFullHpIsRefusedAndNoTurnIsSpent() {
        Game game = singleEnemyGame(60, 5);

        game.playerUsePotion();

        assertTrue(happened(new ActionRefused(RefusalReason.FULL_HP)));
        assertFalse(game.isEnemyTurnPending());
        assertEquals(new GameState(1, 100, 2), game.snapshot());
        assertEquals(60, game.getCurrentEnemy().getCurrentHp());
    }

    @Test
    void potionHealsAndPassesTheTurn() {
        Game game = singleEnemyGame(1000, 50);

        game.playerAttack();
        game.enemyTurn();
        assertEquals(50, game.snapshot().currentHp());

        game.playerUsePotion();
        assertTrue(game.isEnemyTurnPending());
        game.enemyTurn();

        assertEquals(40, game.snapshot().currentHp());
        assertEquals(1, game.snapshot().potions());
    }

    @Test
    void potionsRunOut() {
        Game game = singleEnemyGame(1000, 30);

        game.playerAttack();
        game.enemyTurn();
        game.playerUsePotion();
        game.enemyTurn();
        game.playerUsePotion();
        game.enemyTurn();
        game.playerUsePotion();

        assertEquals(0, game.snapshot().potions());
        assertTrue(happened(new ActionRefused(RefusalReason.NO_POTIONS)));
        assertEquals(70, game.snapshot().currentHp());
    }

    @Test
    void activeAbilityIsRefusedBeforeItIsUnlocked() {
        Game game = singleEnemyGame(60, 5);

        game.playerUseActiveAbility();

        assertTrue(happened(new ActionRefused(RefusalReason.NO_ABILITY)));
        assertFalse(game.isEnemyTurnPending());
        assertEquals(60, game.getCurrentEnemy().getCurrentHp());
        assertEquals(100, game.snapshot().currentHp());
    }

    @Test
    void statusDescribesHeroAndEnemy() {
        Game game = singleEnemyGame(60, 5);

        game.playerAttack();
        game.enemyTurn();
        GameStatus status = game.status();

        assertEquals(1, status.levelNumber());
        assertEquals("Nox", status.heroName());
        assertEquals(95, status.heroHp());
        assertEquals(100, status.heroMaxHp());
        assertEquals(2, status.potions());
        assertFalse(status.abilityAvailable());
        assertEquals("Mostro", status.enemyName());
        assertEquals(45, status.enemyHp());
        assertEquals(60, status.enemyMaxHp());
        assertFalse(status.finished());
        assertFalse(status.won());
    }

    @Test
    void statusReportsVictory() {
        Game game = singleEnemyGame(15, 5);

        game.playerAttack();

        assertTrue(game.status().finished());
        assertTrue(game.status().won());
        assertTrue(game.isWon());
    }

    @Test
    void statusReportsDefeat() {
        Game game = singleEnemyGame(1000, 100);

        game.playerAttack();
        game.enemyTurn();

        assertTrue(game.status().finished());
        assertFalse(game.status().won());
        assertFalse(game.isWon());
    }

    private static class FakeLevelProvider implements LevelProvider {

        private final List<Supplier<Level>> levels;

        FakeLevelProvider(List<Supplier<Level>> levels) {
            this.levels = levels;
        }

        @Override
        public Level createLevel(int number) {
            return levels.get(number - 1).get();
        }

        @Override
        public int getTotalLevels() {
            return levels.size();
        }
    }
}