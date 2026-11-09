package it.unicam.cs.mpgc.rpg129873.ability;

import it.unicam.cs.mpgc.rpg129873.model.Entity;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

public class FuriaShimmer implements ActiveAbility{
    private int uses = 0;

    @Override
    public String getName(){
        return "Furia Shimmer";
    }

    @Override
    public void use(Hero owner, Entity target){
        int damage = owner.getAttackPower();
        target.takeDamage(damage);
        target.takeDamage(damage);
        uses++;

        if (uses % 3 == 0){
            owner.takeDamage(5);
        }
    }
}
