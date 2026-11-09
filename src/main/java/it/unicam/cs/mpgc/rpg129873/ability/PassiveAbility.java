package it.unicam.cs.mpgc.rpg129873.ability;

import it.unicam.cs.mpgc.rpg129873.model.Hero;

public interface PassiveAbility {
    String getName();
    default void onTurnStart(Hero owner, int turn){
    }
    default int modifyDamage(int damage, int turn){
        return damage;
    }
}
