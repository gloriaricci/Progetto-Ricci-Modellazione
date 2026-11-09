package it.unicam.cs.mpgc.rpg129873.level;

import it.unicam.cs.mpgc.rpg129873.model.enemy.*;
import it.unicam.cs.mpgc.rpg129873.reward.*;

import java.util.List;
import java.util.function.Supplier;

public class LevelFactory {

    private static final List<Supplier<Level>> LEVELS = List.of(
            () -> new Level(1, List.of(new CollezionistaDiOssa()), new RewardScattoRapido()),
            () -> new Level(2, List.of(new MadreBrassmonkey()), new RewardSpada()),
            () -> new Level(3, List.of(new IGemelli("boss3a", "Gemello 1"),
                    new IGemelli("boss3b", "Gemello 2")),
                    new RewardSpadaTemplari()),
            () -> new Level(4, List.of(new DuchessaDiSangue()), new RewardFuriaShimmer()),
            () -> new Level(5, List.of(new BaroneShimmer()), new RewardNessuna())
    );

    public static Level createLevel(int number) {
        if (number < 1 || number > LEVELS.size()) {
            throw new IllegalArgumentException("Invalid level: " + number);
        }
        return LEVELS.get(number - 1).get();
    }

    public static int getTotalLevels() {
        return LEVELS.size();
    }
}
