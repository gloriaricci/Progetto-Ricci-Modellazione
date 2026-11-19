package it.unicam.cs.mpgc.rpg129873.view.component;

import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * Fa rispondere il nemico dopo una pausa fissa. Non programma nulla se la risposta non è
 * in sospeso, se ce n'è già una programmata o se {@code blocked} è vero (un annuncio
 * è a schermo): in quel caso la vista richiama {@link #scheduleIfNeeded} alla chiusura.
 */
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
