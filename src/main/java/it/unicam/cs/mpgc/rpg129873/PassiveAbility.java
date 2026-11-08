package it.unicam.cs.mpgc.rpg129873;

public interface PassiveAbility {
    String getName();
    default void onTurnStart(Hero owner, int turn){
    }
    default int modifyDamage(int damage, int turn){
        return damage;
    }
}
