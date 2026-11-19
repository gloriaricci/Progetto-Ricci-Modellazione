package it.unicam.cs.mpgc.rpg129873.model;

import it.unicam.cs.mpgc.rpg129873.ability.ActiveAbility;
import it.unicam.cs.mpgc.rpg129873.ability.PassiveAbility;
import it.unicam.cs.mpgc.rpg129873.weapon.Weapon;

/**
 * Eroe controllato dal giocatore. Ha una sola arma, una sola abilità passiva e una
 * sola abilità attiva: ogni nuova ricompensa sostituisce quella precedente, non c'è
 * inventario. Tiene il conteggio dei turni giocati ({@code turnCount}), che serve alle
 * abilità passive; il conteggio non viene salvato e riparte da zero dopo un caricamento.
 */

public class Hero extends Entity {
    private Weapon currentWeapon;
    private PassiveAbility passive;
    private ActiveAbility activeAbility;

    private int turnCount = 0;

    public Hero(String id, String name, int maxHp, int attackPower) {
        super(id, name, maxHp, attackPower);
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
    }

    public PassiveAbility getPassive() {
        return passive;
    }

    public void setPassive(PassiveAbility passive) {
        this.passive = passive;
    }

    public ActiveAbility getActiveAbility() {
        return activeAbility;
    }

    public void setActiveAbility(ActiveAbility a) {
        this.activeAbility = a;
    }

    public boolean hasActiveAbility() {
        return activeAbility != null;
    }

    /**
     * Da chiamare all'inizio di ogni turno in cui il giocatore attacca o usa
     * l'abilità (non quando beve una pozione). Incrementa il contatore, quindi
     * la prima azione ha turno 1, e dà alla passiva la possibilità di agire (per esempio curare).
     */
    public void onTurnStart() {
        turnCount++;

        if (passive != null) {
            passive.onTurnStart(this, turnCount);
        }
    }

    @Override
    /**
     * Danno di un attacco: potenza di base (0 per Nox) più il danno dell'arma, poi
     * modificato dalla passiva. Il risultato dipende dal turno corrente, quindi due
     * chiamate in turni diversi possono dare valori diversi (vedi {@code ScattoRapido}).
     */
    public int getAttackPower() {
        int damage = super.getAttackPower();
        if (currentWeapon != null) {
            damage += currentWeapon.getDamage();
        }

        if (passive != null) {
            damage = passive.modifyDamage(damage, turnCount);
        }

        return damage;
    }

    public void attack(Enemy enemy) {
        if (enemy != null) {
            enemy.takeDamage(getAttackPower());
        }
    }

    /**
     * @return {@code false} se l'eroe non ha ancora un'abilità attiva (non succede nulla)
     */
    public boolean useActiveAbility(Entity target) {
        if (activeAbility == null) {
            return false;
        }
        activeAbility.use(this, target);
        return true;
    }

}

