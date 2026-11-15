package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

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
