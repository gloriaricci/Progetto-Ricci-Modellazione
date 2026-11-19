package it.unicam.cs.mpgc.rpg129873.model;

import java.util.Optional;

/**
 * Nemico base: attacca l'eroe infliggendo la propria potenza d'attacco. I boss con
 * una meccanica speciale ridefiniscono {@link #attack} e segnalano l'effetto con
 * {@link #setEvent}, in modo che il dominio descriva cosa è successo ({@link EnemyEffect})
 * senza produrre testo da mostrare.
 */

public class Enemy extends Entity {
    private EnemyEffect pendingEvent;

    public Enemy(String id, String name, int maxHp, int attackPower) {
        super(id, name, maxHp, attackPower);
    }

    public void attack(Hero hero) {
        hero.takeDamage(this.getAttackPower());
    }

    protected void setEvent(EnemyEffect effect) {
        this.pendingEvent = effect;
    }

    /**
     * Restituisce l'effetto speciale dell'ultimo attacco, se c'è stato, e lo cancella:
     * ogni effetto si legge una sola volta. {@code Game} lo chiama subito dopo l'attacco
     * per notificarlo come evento. Se un attacco imposta più effetti, resta solo l'ultimo.
     */
    public Optional<EnemyEffect> consumeEvent() {
        EnemyEffect effect = pendingEvent;
        pendingEvent = null;
        return Optional.ofNullable(effect);
    }
}
