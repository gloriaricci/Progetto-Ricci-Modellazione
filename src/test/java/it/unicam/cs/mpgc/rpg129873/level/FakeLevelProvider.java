package it.unicam.cs.mpgc.rpg129873.level;

import java.util.List;
import java.util.function.Supplier;

public class FakeLevelProvider implements LevelProvider {

    private final List<Supplier<Level>> levels;

    public FakeLevelProvider(List<Supplier<Level>> levels) {
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
