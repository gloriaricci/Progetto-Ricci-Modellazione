package it.unicam.cs.mpgc.rpg129873.view.component;

import java.util.Objects;
import java.util.function.BooleanSupplier;

public class EnemyTurnScheduler {

    private static final double DELAY_MS = 700;

    private final BooleanSupplier turnPending;
    private final Runnable playEnemyTurn;
    private final DelayedTask task = new DelayedTask();

    public EnemyTurnScheduler(BooleanSupplier turnPending, Runnable playEnemyTurn) {
        this.turnPending = Objects.requireNonNull(turnPending);
        this.playEnemyTurn = Objects.requireNonNull(playEnemyTurn);
    }

    public void scheduleIfNeeded(boolean blocked) {
        if (blocked || !turnPending.getAsBoolean() || task.isRunning()) {
            return;
        }
        task.schedule(DELAY_MS, playEnemyTurn);
    }

    public void cancel() {
        task.cancel();
    }
}
