package it.unicam.cs.mpgc.rpg129873.reward;

import it.unicam.cs.mpgc.rpg129873.model.Hero;

/**
 * Ricompensa assegnata all'eroe quando sconfigge l'ultimo nemico di un livello. Una
 * ricompensa sostituisce arma o abilità precedenti, non le accumula, e può cambiarne
 * più di una insieme (la Grande Spada dei Templari porta anche la sua passiva).
 * <p>
 * Non emette eventi: {@code Game} confronta l'equipaggiamento prima e dopo
 * (vedi {@code Loadout}) e annuncia da solo ciò che è cambiato. Deve dipendere solo
 * dall'eroe, perché {@code GameFactory.restore} rigioca in ordine le ricompense dei livelli
 * già superati per ricostruire l'equipaggiamento di un salvataggio.
 */
public interface Reward {
    void apply(Hero hero);
}
