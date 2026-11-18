package it.unicam.cs.mpgc.rpg129873.model;

import java.util.Objects;

public record EnemyEffect(Kind kind, int amount) {

    public enum Kind {LIFE_STEAL, TIME_DISTORTION}

    public EnemyEffect {
        Objects.requireNonNull(kind);
    }
}
