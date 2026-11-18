package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SpriteConfigLoaderTest {

    @Test
    void theBundledConfigurationDescribesHeroAndAllBosses() {
        Map<String, SpriteSpec> specs = new SpriteConfigLoader().load();

        for (String id : List.of("nox", "boss1", "boss2", "boss3a", "boss3b", "boss4", "bossFinale")) {
            assertTrue(specs.containsKey(id), "missing sprite: " + id);
            assertTrue(specs.get(id).animations().containsKey(SpriteSpec.IDLE));
        }
    }

    @Test
    void aMissingFileGivesAnEmptyConfiguration() {
        assertTrue(new SpriteConfigLoader("/non-esiste.json").load().isEmpty());
    }
}