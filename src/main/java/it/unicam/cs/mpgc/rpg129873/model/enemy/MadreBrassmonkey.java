package it.unicam.cs.mpgc.rpg129873.model.enemy;

import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

public class MadreBrassmonkey extends Enemy {
    private int attackTurns = 0;

    public MadreBrassmonkey(){
        super("boss2", "Madre Brassmonkey", 140, 20);
    }

    @Override
    public void attack(Hero hero){
        attackTurns++;
        super.attack(hero);

        if (attackTurns >= 2){
            //effetto speciale da aggiungere schermo nero
            attackTurns = 0;
        }
    }
}

