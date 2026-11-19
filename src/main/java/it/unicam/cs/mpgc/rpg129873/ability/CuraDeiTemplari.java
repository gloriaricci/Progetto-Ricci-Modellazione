package it.unicam.cs.mpgc.rpg129873.ability;

import it.unicam.cs.mpgc.rpg129873.model.Hero;

/**
 * Passiva: ogni terzo turno dell'eroe, prima dell'azione, recupera 15 punti vita.
 */

public class CuraDeiTemplari implements PassiveAbility {
    @Override
    public String getName() {
        return "Cura dei Templari";
    }

    @Override
    public void onTurnStart(Hero owner, int turn) {
        if (turn % 3 == 0) {
            owner.heal(15);
        }
    }
}
