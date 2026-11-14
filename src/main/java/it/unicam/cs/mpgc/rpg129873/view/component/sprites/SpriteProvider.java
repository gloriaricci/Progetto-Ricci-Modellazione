package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import java.util.Optional;

public interface SpriteProvider {
    Optional<Sprite> spriteFor(String id);
}
