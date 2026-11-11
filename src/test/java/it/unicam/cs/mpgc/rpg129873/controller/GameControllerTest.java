package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.Game;
import it.unicam.cs.mpgc.rpg129873.game.GameFactory;
import it.unicam.cs.mpgc.rpg129873.game.GameState;
import it.unicam.cs.mpgc.rpg129873.level.Level;
import it.unicam.cs.mpgc.rpg129873.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.persistence.GameRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private final InMemoryRepository repository = new InMemoryRepository();

    private GameController controller(List<Supplier<Level>> levels) {
        Game game = new GameFactory(new FakeLevelProvider(levels), message -> { }).newGame();
        return new GameController(game, repository);
    }

    @Test
    void passingToTheNextLevelSavesACheckpoint() {
        GameController controller = controller(List.of(
                () -> new Level(1, List.of(new Enemy("a", "Goblin", 15, 5)), hero -> { }),
                () -> new Level(2, List.of(new Enemy("b", "Orco", 1000, 5)), hero -> { })));

        controller.onAttack();

        assertEquals(Optional.of(new GameState(2, 140, 2)), repository.load());
    }

    @Test
    void aNormalAttackDoesNotSave() {
        GameController controller = controller(List.of(
                () -> new Level(1, List.of(new Enemy("a", "Goblin", 1000, 5)), hero -> { })));

        controller.onAttack();

        assertTrue(repository.load().isEmpty());
    }

    @Test
    void winningTheGameClearsTheSave() {
        repository.save(new GameState(1, 100, 2));
        GameController controller = controller(List.of(
                () -> new Level(1, List.of(new Enemy("a", "Goblin", 15, 5)), hero -> { })));

        controller.onAttack();

        assertTrue(controller.isFinished());
        assertTrue(controller.getStatus().won());
        assertTrue(repository.load().isEmpty());
    }

    @Test
    void losingKeepsTheCheckpoint() {
        repository.save(new GameState(1, 100, 2));
        GameController controller = controller(List.of(
                () -> new Level(1, List.of(new Enemy("a", "Goblin", 1000, 100)), hero -> { })));

        controller.onAttack();
        assertTrue(controller.isEnemyTurnPending());
        controller.onEnemyTurn();

        assertTrue(controller.isFinished());
        assertFalse(controller.getStatus().won());
        assertTrue(repository.load().isPresent());
    }

    private static class InMemoryRepository implements GameRepository {

        private GameState saved;

        @Override
        public void save(GameState state) {
            saved = state;
        }

        @Override
        public Optional<GameState> load() {
            return Optional.ofNullable(saved);
        }

        @Override
        public void clear() {
            saved = null;
        }
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
