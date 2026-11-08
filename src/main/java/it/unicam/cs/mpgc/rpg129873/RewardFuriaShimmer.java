package it.unicam.cs.mpgc.rpg129873;

public class RewardFuriaShimmer implements Reward{
    @Override
    public void apply(Hero hero){
        hero.setActiveAbility(new FuriaShimmer());
    }
}
