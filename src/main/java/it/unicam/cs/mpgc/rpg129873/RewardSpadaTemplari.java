package it.unicam.cs.mpgc.rpg129873;

public class RewardSpadaTemplari implements Reward {
    @Override
    public void apply(Hero hero){
        hero.setWeapon(new SpadaTemplari());
        hero.setPassive(new CuraDeiTemplari());
    }
}
