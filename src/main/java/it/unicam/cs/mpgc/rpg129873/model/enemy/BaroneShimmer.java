package it.unicam.cs.mpgc.rpg129873.model.enemy;

import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.model.EnemyEffect;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

/**
 * Boss finale. Ogni due attacchi recupera 20 punti vita (distorsione temporale) e lo
 * segnala con {@code EnemyEffect.Kind.TIME_DISTORTION}. Il recupero è fisso e non
 * supera mai la vita massima.
 */

public class BaroneShimmer extends Enemy {

    private int turnCounter = 0;
    private static final int HEAL_AMOUNT = 20;

    public BaroneShimmer() {
        super("bossFinale", "Barone dello Shimmer", 300, 35);
    }

    @Override
    public void attack(Hero hero) {
        turnCounter++;

        super.attack(hero);

        if (turnCounter % 2 == 0) {
            heal(HEAL_AMOUNT);
            setEvent(new EnemyEffect(EnemyEffect.Kind.TIME_DISTORTION, HEAL_AMOUNT));
        }
    }
}

