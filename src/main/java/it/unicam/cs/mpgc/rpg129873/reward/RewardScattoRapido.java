package it.unicam.cs.mpgc.rpg129873.reward;

import it.unicam.cs.mpgc.rpg129873.ability.ScattoRapido;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

public class RewardScattoRapido implements Reward{
    @Override
    public void apply (Hero hero){
        hero.setPassive(new ScattoRapido());
    }
}
