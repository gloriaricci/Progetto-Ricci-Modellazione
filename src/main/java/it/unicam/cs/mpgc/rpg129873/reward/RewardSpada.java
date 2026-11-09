package it.unicam.cs.mpgc.rpg129873.reward;

import it.unicam.cs.mpgc.rpg129873.model.Hero;
import it.unicam.cs.mpgc.rpg129873.weapon.Spada;

public class RewardSpada implements Reward{
    @Override
    public void apply(Hero hero){
        hero.setWeapon(new Spada());
    }
}
