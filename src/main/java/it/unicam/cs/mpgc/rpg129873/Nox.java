package it.unicam.cs.mpgc.rpg129873;

public class Nox extends Hero{

    private int potionsCount;
    private int potionHealAmount;

    public Nox(){
        super("eroe Nox", "Nox", 100, 0);
        this.potionsCount = 2;
        this.potionHealAmount = 40;
    }

    public void attacca(Enemy enemy){
        if(enemy != null ){
            enemy.takeDamage(getAttackPower());
            System.out.println(getName()+ "attacca" + enemy.getName() + "infliggendo" + getAttackPower() + "danni.");
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
