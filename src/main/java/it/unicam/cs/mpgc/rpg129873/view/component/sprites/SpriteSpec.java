package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import java.util.Map;

public record SpriteSpec(String file, int frameWidth, int frameHeight,
                         Map<String, AnimationSpec> animations) {

    public static final String IDLE = "idle";

    public SpriteSpec {
        if (file == null || file.isBlank()) {
            throw new IllegalArgumentException("The sprite file cannot be null or blank");
        }
        if (frameWidth < 1 || frameHeight < 1) {
            throw new IllegalArgumentException("Invalid frame size: " + frameWidth + "x" + frameHeight);
        }
        if (animations == null || !animations.containsKey(IDLE)) {
            throw new IllegalArgumentException("A sprite needs at least the '" + IDLE + "' animation");
        }
        if (!animations.get(IDLE).loop()) {
            throw new IllegalArgumentException("The '" + IDLE + "' animation must loop");
        }
        animations = Map.copyOf(animations);
    }
}
