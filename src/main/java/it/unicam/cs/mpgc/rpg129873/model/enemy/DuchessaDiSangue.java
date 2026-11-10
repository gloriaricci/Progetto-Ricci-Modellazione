package it.unicam.cs.mpgc.rpg129873.model.enemy;

import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

public class DuchessaDiSangue extends Enemy {
    private int turnCounter;

    public DuchessaDiSangue(){
        super("boss4", "Duchessa di Sangue", 220, 30);
    }

    @Override
    public void attack(Hero hero){
        turnCounter++;

        super.attack(hero);

        if (turnCounter >= 2){
            int lifeSteal = 10;
            this.heal(lifeSteal);
            turnCounter = 0;
        }
    }
}

