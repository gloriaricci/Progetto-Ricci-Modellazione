package it.unicam.cs.mpgc.rpg129873.ability;

/**
 * Passiva: nei turni pari (il secondo, il quarto...) l'attacco fa 5 danni in più.
 */

public class ScattoRapido implements PassiveAbility {
    @Override
    public String getName() {
        return "Scatto Rapido";
    }

    @Override
    public int modifyDamage(int damage, int turn) {
        return (turn % 2 == 0) ? damage + 5 : damage;
    }
}
