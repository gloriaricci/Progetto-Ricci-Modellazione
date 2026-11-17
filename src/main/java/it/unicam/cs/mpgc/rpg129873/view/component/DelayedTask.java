package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.Objects;

public class DelayedTask {

    private PauseTransition pause;

    public void schedule(double millis, Runnable action) {
        Objects.requireNonNull(action);
        cancel();
        pause = new PauseTransition(Duration.millis(millis));
        pause.setOnFinished(e -> action.run());
        pause.play();
    }

    public boolean isRunning() {
        return pause != null && pause.getStatus() == Animation.Status.RUNNING;
    }

    public void cancel() {
        if (pause != null) {
            pause.stop();
        }
    }
}
