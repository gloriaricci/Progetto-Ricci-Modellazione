package it.unicam.cs.mpgc.rpg129873.view.component.sprites;

import java.util.Map;
import java.util.Objects;

public class FramePlayer {

    private final Map<String, AnimationSpec> animations;
    private AnimationSpec current;
    private int frameIndex;

    public FramePlayer(Map<String, AnimationSpec> animations) {
        Objects.requireNonNull(animations);
        if (!animations.containsKey(SpriteSpec.IDLE)) {
            throw new IllegalArgumentException(
                    "A player needs at least the '" + SpriteSpec.IDLE + "' animation");
        }
        this.animations = Map.copyOf(animations);
        start(this.animations.get(SpriteSpec.IDLE));
    }

    public boolean play(String animationName) {
        AnimationSpec animation = animations.get(animationName);
        if (animation == null) {
            return false;
        }
        start(animation);
        return true;
    }

    public boolean advance() {
        if (frameIndex + 1 < current.frames()) {
            frameIndex++;
            return false;
        }
        if (current.loop()) {
            frameIndex = 0;
            return false;
        }
        start(animations.get(SpriteSpec.IDLE));
        return true;
    }

    public int row() {
        return current.row();
    }

    public int column() {
        return current.startFrame() + frameIndex;
    }

    public int frameMillis() {
        return current.frameMillis();
    }

    /** Serve un timer solo se c'è qualcosa da far avanzare o da far tornare a idle. */
    public boolean needsTimer() {
        return current.frames() > 1 || !current.loop();
    }

    private void start(AnimationSpec animation) {
        current = animation;
        frameIndex = 0;
    }
}