package it.unicam.cs.mpgc.rpg129873;

public class RewardScattoRapido implements Reward{
    @Override
    public void apply (Hero hero){
        hero.setPassive(new ScattoRapido());
    }
}
