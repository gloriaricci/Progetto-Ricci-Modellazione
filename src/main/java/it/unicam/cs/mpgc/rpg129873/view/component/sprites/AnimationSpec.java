package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

/**
 * Una animazione dentro lo sprite sheet, letta da {@code sprites.json}. I frame sono
 * celle di una griglia: {@code row} è la riga (da 0), {@code startFrame} la colonna da cui
 * si parte (da 0), {@code frames} quanti frame consecutivi seguono verso destra,
 * {@code frameMillis} la durata di ciascuno. Se {@code loop} è falso l'animazione
 * si ferma sull'ultimo frame e si torna a idle. {@code startFrame} è facoltativo e vale 0
 * se manca.
 */
public record AnimationSpec(int row, int frames, int frameMillis, boolean loop, int startFrame) {

    public AnimationSpec(int row, int frames, int frameMillis, boolean loop) {
        this(row, frames, frameMillis, loop, 0);
    }

    public AnimationSpec {
        if (row < 0) {
            throw new IllegalArgumentException("Invalid row: " + row);
        }
        if (frames < 1) {
            throw new IllegalArgumentException("Invalid frames: " + frames);
        }
        if (frameMillis < 1) {
            throw new IllegalArgumentException("Invalid frame duration: " + frameMillis);
        }
        if (startFrame < 0) {
            throw new IllegalArgumentException("Invalid start frame: " + startFrame);
        }
    }
}
