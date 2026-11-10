package it.unicam.cs.mpgc.rpg129873.level;

import it.unicam.cs.mpgc.rpg129873.reward.RewardNessuna;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelFactoryTest {

    private final LevelFactory factory = new LevelFactory();

    @Test
    void thereAreFiveLevels() {
        assertEquals(5, factory.getTotalLevels());
    }

    @Test
    void levelNumbersMatchTheRequestedOnes() {
        for (int i = 1; i <= 5; i++) {
            assertEquals(i, factory.createLevel(i).getNumber());
        }
    }

    @Test
    void eachLevelStartsWithItsFirstBoss() {
        String[] bosses = {
                "Collezionista di ossa", "Madre Brassmonkey", "Gemello 1",
                "Duchessa di Sangue", "Barone dello Shimmer"
        };
        for (int i = 1; i <= 5; i++) {
            assertEquals(bosses[i - 1], factory.createLevel(i).getCurrentEnemy().getName());
        }
    }

    @Test
    void invalidLevelsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> factory.createLevel(0));
        assertThrows(IllegalArgumentException.class, () -> factory.createLevel(6));
        assertThrows(IllegalArgumentException.class, () -> factory.createLevel(-1));
    }

    @Test
    void everyCallReturnsAFreshLevel() {
        Level first = factory.createLevel(1);
        first.getCurrentEnemy().takeDamage(1000);

        Level second = factory.createLevel(1);

        assertNotSame(first, second);
        assertTrue(second.getCurrentEnemy().isAlive());
    }

    @Test
    void levelThreeHasTwoTwinsFoughtInOrder() {
        Level level = factory.createLevel(3);

        assertEquals("Gemello 1", level.getCurrentEnemy().getName());
        assertTrue(level.hasNextEnemy());

        level.nextEnemy();

        assertEquals("Gemello 2", level.getCurrentEnemy().getName());
        assertFalse(level.hasNextEnemy());
    }

    @Test
    void singleBossLevelsHaveNoNextEnemy() {
        for (int i : new int[]{1, 2, 4, 5}) {
            assertFalse(factory.createLevel(i).hasNextEnemy());
        }
    }

    @Test
    void finalLevelGivesNoReward() {
        assertInstanceOf(RewardNessuna.class, factory.createLevel(5).getReward());
    }
}
