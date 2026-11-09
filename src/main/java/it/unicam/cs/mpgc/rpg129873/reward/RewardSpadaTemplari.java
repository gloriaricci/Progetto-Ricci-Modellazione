package it.unicam.cs.mpgc.rpg129873.reward;

import it.unicam.cs.mpgc.rpg129873.ability.CuraDeiTemplari;
import it.unicam.cs.mpgc.rpg129873.model.Hero;
import it.unicam.cs.mpgc.rpg129873.weapon.SpadaTemplari;

public class RewardSpadaTemplari implements Reward {
    @Override
    public void apply(Hero hero){
        hero.setWeapon(new SpadaTemplari());
        hero.setPassive(new CuraDeiTemplari());
    }
}
