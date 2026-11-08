package it.unicam.cs.mpgc.rpg129873;

public class RewardSpada implements Reward{
    @Override
    public void apply(Hero hero){
        hero.setWeapon(new Spada());
    }
}
