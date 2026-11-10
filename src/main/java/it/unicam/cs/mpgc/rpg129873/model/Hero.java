package it.unicam.cs.mpgc.rpg129873.model;

import it.unicam.cs.mpgc.rpg129873.ability.ActiveAbility;
import it.unicam.cs.mpgc.rpg129873.ability.PassiveAbility;
import it.unicam.cs.mpgc.rpg129873.weapon.Weapon;

public class Hero extends Entity {
    private Weapon currentWeapon;
    private PassiveAbility passive;
    private ActiveAbility activeAbility;

    private int turnCount = 0;
    public Hero(String id, String name, int maxHp,int attackPower){
        super(id, name, maxHp,attackPower);
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }
    public void setWeapon(Weapon weapon){
        this.currentWeapon = weapon;
    }

    public PassiveAbility getPassive(){
        return passive;
    }
    public void setPassive(PassiveAbility passive){
        this.passive = passive;
    }

    public ActiveAbility getActiveAbility(){
        return activeAbility;
    }
    public void setActiveAbility(ActiveAbility a){
        this.activeAbility = a;
    }
    public boolean hasActiveAbility(){
        return activeAbility != null;
    }

    public void onTurnStart(){
        turnCount++;

        if (passive != null){
            passive.onTurnStart(this, turnCount);
        }
    }

    @Override
    public int getAttackPower(){
        int damage = super.getAttackPower();
        if (currentWeapon != null){
            damage+= currentWeapon.getDamage();
        }

        if (passive != null){
            damage = passive.modifyDamage(damage, turnCount);
        }

        return damage;
    }

    public void attack(Enemy enemy){
        if (enemy != null){
            enemy.takeDamage(getAttackPower());
        }
    }

    public boolean useActiveAbility(Entity target){
        if (activeAbility == null){
            return false;
        }
        activeAbility.use(this, target);
        return true;
    }

}

