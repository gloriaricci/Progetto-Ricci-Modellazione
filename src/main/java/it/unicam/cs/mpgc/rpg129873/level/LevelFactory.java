package it.unicam.cs.mpgc.rpg129873.level;

import it.unicam.cs.mpgc.rpg129873.model.enemy.*;
import it.unicam.cs.mpgc.rpg129873.reward.*;

import java.util.List;
import java.util.function.Supplier;

/**
 * Elenco dei cinque livelli del gioco, definito nel codice. Per aggiungere un livello si
 * aggiunge una voce a {@code LEVELS}: la posizione nella lista è il numero del livello e
 * deve coincidere con quello passato a {@link Level}. Per lo sprite dei nuovi nemici
 * serve una voce in {@code sprites.json} con lo stesso id (lo sfondo è facoltativo, ne basta uno
 * chiamato {@code level<N>.gif}). La vita dell'eroe segue {@code HealthProgression}.
 * Per leggere i livelli da file basta un'altra implementazione di {@link LevelProvider}.
 */
public class LevelFactory implements LevelProvider {

    // Ogni voce è un Supplier e non un Level già costruito, così ogni createLevel()
    // crea nemici nuovi a vita piena.
    private static final List<Supplier<Level>> LEVELS = List.of(
            () -> new Level(1, List.of(new CollezionistaDiOssa()), new RewardScattoRapido()),
            () -> new Level(2, List.of(new MadreBrassmonkey()), new RewardSpada()),
            () -> new Level(3, List.of(new IGemelli("boss3a", "Gemello 1"),
                    new IGemelli("boss3b", "Gemello 2")),
                    new RewardSpadaTemplari()),
            () -> new Level(4, List.of(new DuchessaDiSangue()), new RewardFuriaShimmer()),
            () -> new Level(5, List.of(new BaroneShimmer()), new RewardNessuna())
    );

    @Override
    public Level createLevel(int number) {
        if (number < 1 || number > LEVELS.size()) {
            throw new IllegalArgumentException("Invalid level: " + number);
        }
        return LEVELS.get(number - 1).get();
    }

    @Override
    public int getTotalLevels() {
        return LEVELS.size();
    }
}
