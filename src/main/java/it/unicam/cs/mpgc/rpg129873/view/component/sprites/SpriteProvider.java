package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import java.util.Optional;

/** Fornisce lo sprite di un'entità, cercato per id. {@code Optional.empty()} significa "nessuno sprite": la vista mostra un riquadro colorato. */
public interface SpriteProvider {
    Optional<Sprite> spriteFor(String id);
}
