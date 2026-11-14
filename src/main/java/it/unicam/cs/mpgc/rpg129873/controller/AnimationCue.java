package it.unicam.cs.mpgc.rpg129873.controller;

import java.util.Objects;

public record AnimationCue(Actor actor, String animation) {

    public enum Actor { HERO, ENEMY }

    public AnimationCue {
        Objects.requireNonNull(actor);
        if (animation == null || animation.isBlank()) {
            throw new IllegalArgumentException("The animation name cannot be null or blank");
        }
    }
}
