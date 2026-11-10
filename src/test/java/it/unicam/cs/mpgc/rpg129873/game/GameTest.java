package it.unicam.cs.mpgc.rpg129873.game;

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

    private final List<String> messages = new ArrayList<>();
    private final AtomicBoolean rewardApplied = new AtomicBoolean(false);

    private Game newGame(List<Supplier<Level>> levels) {
        LevelProvider provider = new FakeLevelProvider(levels);
        return new GameFactory(provider, messages::add).newGame();
    }

    private Game singleEnemyGame(int enemyHp, int enemyAttack) {
        return newGame(List.of(
                () -> new Level(1, List.of(new Enemy("e1", "Mostro", enemyHp, enemyAttack)), hero -> { })));
    }

    private boolean saidSomethingContaining(String fragment) {
        return messages.stream().anyMatch(m -> m.contains(fragment));
    }

    @Test
    void startAnnouncesTheFirstLevel() {
        Game game = singleEnemyGame(60, 5);
        game.start();
        assertTrue(saidSomethingContaining("Inizio Livello 1: Mostro"));
    }

    @Test
    void snapshotReflectsLevelHpAndPotions() {
        Game game = singleEnemyGame(60, 5);
        assertEquals(new GameState(1, 100, 2), game.snapshot());
    }

    @Test
    void attackDamagesTheEnemyAndTheEnemyHitsBack() {
        Game game = singleEnemyGame(60, 5);

        game.playerAttack();

        assertEquals(45, game.getCurrentEnemy().getCurrentHp());
        assertEquals(95, game.snapshot().currentHp());
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
    void defeatingTheLastBossWinsTheGame() {
        Game game = singleEnemyGame(30, 5);

        game.playerAttack();
        assertFalse(game.isFinished());
        game.playerAttack();

        assertTrue(game.isFinished());
        assertTrue(saidSomethingContaining("Hai vinto"));
    }

    @Test
    void nothingHappensAfterTheGameIsFinished() {
        Game game = singleEnemyGame(15, 5);
        game.playerAttack();
        int messagesAtTheEnd = messages.size();

        game.playerAttack();
        game.playerUsePotion();
        game.playerUseActiveAbility();

        assertEquals(messagesAtTheEnd, messages.size());
    }

    @Test
    void beingDefeatedEndsTheGame() {
        Game game = singleEnemyGame(1000, 100);

        game.playerAttack();

        assertTrue(game.isFinished());
        assertTrue(saidSomethingContaining("Game over"));
    }

    @Test
    void potionAtFullHpIsRefusedAndNoTurnIsSpent() {
        Game game = singleEnemyGame(60, 5);

        game.playerUsePotion();

        assertTrue(saidSomethingContaining("al massimo"));
        assertEquals(new GameState(1, 100, 2), game.snapshot());
        assertEquals(60, game.getCurrentEnemy().getCurrentHp());
    }

    @Test
    void potionHealsAndPassesTheTurn() {
        Game game = singleEnemyGame(1000, 50);

        game.playerAttack();
        assertEquals(50, game.snapshot().currentHp());

        game.playerUsePotion();

        assertEquals(40, game.snapshot().currentHp());
        assertEquals(1, game.snapshot().potions());
    }

    @Test
    void potionsRunOut() {
        Game game = singleEnemyGame(1000, 30);

        game.playerAttack();
        game.playerUsePotion();
        game.playerUsePotion();
        game.playerUsePotion();

        assertEquals(0, game.snapshot().potions());
        assertTrue(saidSomethingContaining("Non hai più"));
        assertEquals(70, game.snapshot().currentHp());
    }

    @Test
    void activeAbilityIsRefusedBeforeItIsUnlocked() {
        Game game = singleEnemyGame(60, 5);

        game.playerUseActiveAbility();

        assertTrue(saidSomethingContaining("nessuna"));
        assertEquals(60, game.getCurrentEnemy().getCurrentHp());
        assertEquals(100, game.snapshot().currentHp());
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
