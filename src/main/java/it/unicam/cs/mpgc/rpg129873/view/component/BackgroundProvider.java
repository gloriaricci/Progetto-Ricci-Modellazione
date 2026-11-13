package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.scene.image.Image;

import java.util.Optional;

public interface BackgroundProvider {
    Optional<Image> imageFor(int level);
}
