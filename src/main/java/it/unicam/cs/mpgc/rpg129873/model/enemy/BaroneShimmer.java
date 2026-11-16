package it.unicam.cs.mpgc.rpg129873.model.enemy;

import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

public class BaroneShimmer extends Enemy {

    private int turnCounter = 0;
    private static final int HEAL_AMOUNT = 20;

    public BaroneShimmer(){
        super("bossFinale", "Barone dello Shimmer", 300, 35);
    }

    @Override
    public void attack (Hero hero) {
        turnCounter++;

        super.attack(hero);

         if (turnCounter % 2 == 0){
             heal(HEAL_AMOUNT);
             setEvent("Distorsione temporale!! il barone ricarica " + healAmount + " punti vita.");
         }
    }
}

