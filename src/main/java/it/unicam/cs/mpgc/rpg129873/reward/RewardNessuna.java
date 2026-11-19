package it.unicam.cs.mpgc.rpg129873.reward;

import it.unicam.cs.mpgc.rpg129873.model.Hero;

/** Per i livelli senza ricompensa (l'ultimo): {@code apply} non cambia nulla di proposito. */
public class RewardNessuna implements Reward {
    @Override
    public void apply(Hero hero) {

    }
}
