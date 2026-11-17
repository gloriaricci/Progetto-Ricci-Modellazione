package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import java.util.Map;

public record SpriteSpec(String file, int frameWidth, int frameHeight,
                         Map<String, AnimationSpec> animations, int scale) {

    public static final String IDLE = "idle";

    public SpriteSpec(String file, int frameWidth, int frameHeight,
                      Map<String, AnimationSpec> animations) {
        this(file, frameWidth, frameHeight, animations, 0);
    }

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
        if (scale < 0) {
            throw new IllegalArgumentException("Invalid scale: " + scale);
        }
        animations = Map.copyOf(animations);
    }

    public boolean fitsIn(int imageWidth, int imageHeight) {
        return animations.values().stream().allMatch(animation ->
                (animation.startFrame() + animation.frames()) * frameWidth <= imageWidth
                        && (animation.row() + 1) * frameHeight <= imageHeight);
    }
}
