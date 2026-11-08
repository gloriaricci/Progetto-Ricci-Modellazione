package it.unicam.cs.mpgc.rpg129873;

public interface ActiveAbility {
    String getName();
    void use(Hero owner, Entity target);
}
