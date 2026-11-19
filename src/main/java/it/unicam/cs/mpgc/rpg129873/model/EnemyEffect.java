package it.unicam.cs.mpgc.rpg129873.model;

import java.util.Objects;

/**
 * Descrive un effetto speciale di un nemico (tipo e quantità di vita recuperata)
 * come dato, non come testo: la frase da mostrare la compone {@code EventTextFormatter}.
 * Per aggiungere un nuovo tipo di effetto si estende {@link Kind}, e il compilatore
 * segnala gli switch da aggiornare.
 */

public record EnemyEffect(Kind kind, int amount) {

    public enum Kind {LIFE_STEAL, TIME_DISTORTION}

    public EnemyEffect {
        Objects.requireNonNull(kind);
    }
}
