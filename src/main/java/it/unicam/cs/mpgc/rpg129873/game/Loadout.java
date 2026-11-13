package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent.RewardGained;
import it.unicam.cs.mpgc.rpg129873.game.GameEvent.RewardKind;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

import java.util.ArrayList;
import java.util.List;

public record Loadout (String weapon, String passive, String activeAbility){

    public static Loadout of (Hero hero){
        return new Loadout(
                hero.getCurrentWeapon()!= null ? hero.getCurrentWeapon().getName() : null,
                hero.getPassive() != null ? hero.getPassive().getName() : null,
                hero.hasActiveAbility() ? hero.getActiveAbility().getName() : null);
    }

    public List<RewardGained> gainedSince(Loadout before) {
        List<RewardGained> gained = new ArrayList<>();
        if (weapon != null && !weapon.equals(before.weapon())) {
            gained.add(new RewardGained(RewardKind.WEAPON, weapon));
        }
        if (passive != null && !passive.equals(before.passive())) {
            gained.add(new RewardGained(RewardKind.PASSIVE, passive));
        }
        if (activeAbility != null && !activeAbility.equals(before.activeAbility())) {
            gained.add(new RewardGained(RewardKind.ACTIVE, activeAbility));
        }
        return gained;
    }
}
