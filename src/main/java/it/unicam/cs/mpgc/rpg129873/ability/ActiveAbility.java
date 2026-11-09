package it.unicam.cs.mpgc.rpg129873.ability;

import it.unicam.cs.mpgc.rpg129873.model.Entity;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

public interface ActiveAbility {
    String getName();
    void use(Hero owner, Entity target);
}
