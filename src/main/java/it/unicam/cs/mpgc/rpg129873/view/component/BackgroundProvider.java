package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.scene.image.Image;

import java.util.Optional;

/** Fornisce lo sfondo di un livello. {@code Optional.empty()} significa "nessuna immagine": la vista usa un colore pieno. */
public interface BackgroundProvider {
    Optional<Image> imageFor(int level);
}
