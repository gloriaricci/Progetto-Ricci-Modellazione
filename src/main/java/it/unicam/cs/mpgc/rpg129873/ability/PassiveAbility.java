package it.unicam.cs.mpgc.rpg129873.ability;

import it.unicam.cs.mpgc.rpg129873.model.Hero;

/**
 * Abilità sempre attiva dell'eroe, che non richiede azioni del giocatore. L'eroe ne
 * ha una sola alla volta. Le implementazioni non tengono contatori propri: ricevono il
 * numero del turno e decidono in base a quello, per cui restano senza stato e il
 * loro comportamento è prevedibile anche dopo un caricamento.
 */

public interface PassiveAbility {
    String getName();

    /**
     * Chiamato da {@link Hero#onTurnStart()} all'inizio di ogni turno del giocatore in cui
     * attacca o usa l'abilità, prima dell'azione. {@code turn} è il numero del turno
     * dell'eroe: parte da 1 e cresce a ogni azione, senza azzerarsi tra i nemici.
     */
    default void onTurnStart(Hero owner, int turn) {
    }

    /**
     * Chiamato ogni volta che viene calcolato il danno dell'eroe, con {@code turn} del
     * turno corrente. Può essere chiamato più volte nello stesso turno, quindi non deve
     * avere effetti collaterali: restituisce solo il danno modificato. Per default non cambia nulla.
     */
    default int modifyDamage(int damage, int turn) {
        return damage;
    }
}
