package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.level.LevelFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameFactoryTest {

    private final List<String> messages = new ArrayList<>();
    private final GameFactory factory = new GameFactory(new LevelFactory(), messages::add);

    @Test
    void newGameStartsAtLevelOneWithFullStats() {
        Game game = factory.newGame();
        assertEquals(new GameState(1, 100, 2), game.snapshot());
    }

    @Test
    void newGameStartsWithTheDagger() {
        Game game = factory.newGame();

        game.playerAttack();

        assertEquals(85, game.getCurrentEnemy().getCurrentHp());
    }

    @Test
    void restoreRebuildsLevelHpAndPotions() {
        Game game = factory.restore(new GameState(3, 50, 1));
        assertEquals(new GameState(3, 50, 1), game.snapshot());
    }

    @Test
    void restoreReplaysThePreviousRewards() {
        Game game = factory.restore(new GameState(3, 100, 2));

        game.playerAttack();

        assertEquals(78, game.getCurrentEnemy().getCurrentHp());
    }

    @Test
    void restoreAtTheFinalLevelHasTheLatestWeaponAndAbility() {
        Game game = factory.restore(new GameState(5, 100, 2));

        game.playerUseActiveAbility();

        assertEquals(240, game.getCurrentEnemy().getCurrentHp());
    }
}
