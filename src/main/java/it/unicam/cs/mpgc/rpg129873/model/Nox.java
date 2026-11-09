package it.unicam.cs.mpgc.rpg129873.model;

public class Nox extends Hero{

    private int potionsCount;
    private int potionHealAmount;

    public Nox(){
        super("eroe Nox", "Nox", 100, 0);
        this.potionsCount = 2;
        this.potionHealAmount = 40;
    }

    public void attack(Enemy enemy){
        if(enemy != null ){
            enemy.takeDamage(getAttackPower());
        }
    }

    public boolean usePotion(){
        if(potionsCount > 0 && getCurrentHp() < getMaxHp()) {
            potionsCount--;
            heal(potionHealAmount);
            return true;
        }
            return false;
    }

    public int getPotionsCount(){
        return potionsCount;
    }

}
