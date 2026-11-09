package it.unicam.cs.mpgc.rpg129873.reward;

import it.unicam.cs.mpgc.rpg129873.ability.FuriaShimmer;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

public class RewardFuriaShimmer implements Reward{
    @Override
    public void apply(Hero hero){
        hero.setActiveAbility(new FuriaShimmer());
    }
}
