package it.unicam.cs.mpgc.rpg129873.model.enemy;

import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.model.EnemyEffect;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

/**
 * Boss del livello 4. Ogni due attacchi recupera 10 punti vita (furto di vita) e lo
 * segnala con {@code EnemyEffect.Kind.LIFE_STEAL}. Il recupero è fisso: non dipende dal
 * danno inflitto.
 */

public class DuchessaDiSangue extends Enemy {
    private int turnCounter;

    public DuchessaDiSangue() {
        super("boss4", "Duchessa di Sangue", 220, 30);
    }

    @Override
    public void attack(Hero hero) {
        turnCounter++;

        super.attack(hero);

        if (turnCounter >= 2) {
            int lifeSteal = 10;
            this.heal(lifeSteal);
            setEvent(new EnemyEffect(EnemyEffect.Kind.LIFE_STEAL, lifeSteal));
            turnCounter = 0;
        }
    }
}

