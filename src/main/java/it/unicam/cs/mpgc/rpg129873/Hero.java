package it.unicam.cs.mpgc.rpg129873;

public class Hero extends Entity {
    private Weapon currentWeapon;
    private PassiveAbility passive;
    private ActiveAbility activeAbility;

    private int countTurni = 0;
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
        countTurni++;

        if (passive != null){
            passive.onTurnStart(this, countTurni);
        }
    }

    @Override
    public int getAttackPower(){
        int damage = super.getAttackPower();
        if (currentWeapon != null){
            damage+= currentWeapon.getDanno();
        }

        if (passive != null){
            damage = passive.modifyDamage(damage, countTurni);
        }

        return damage;
    }

    public boolean useActiveAbility(Entity target){
        if (activeAbility == null){
            return false;
        }
        activeAbility.use(this, target);
        return true;
    }

}

