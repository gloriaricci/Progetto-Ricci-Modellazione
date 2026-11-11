package it.unicam.cs.mpgc.rpg129873.game;

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

    public List<String> gainedSince(Loadout before) {
        List<String> messages = new ArrayList<>();
        if (weapon != null && !weapon.equals(before.weapon())) {
            messages.add("Hai ottenuto l'arma: " + weapon + "!");
        }
        if (passive != null && !passive.equals(before.passive())) {
            messages.add("Hai ottenuto l'abilità passiva: " + passive + "!");
        }
        if (activeAbility != null && !activeAbility.equals(before.activeAbility())) {
            messages.add("Hai ottenuto l'abilità attiva: " + activeAbility + "!");
        }
        return messages;
    }
}
