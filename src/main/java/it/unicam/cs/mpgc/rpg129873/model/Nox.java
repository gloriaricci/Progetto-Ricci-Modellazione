package it.unicam.cs.mpgc.rpg129873.model;

public class Nox extends Hero implements PotionHolder{

    private int potionsCount;
    private final int potionHealAmount;

    public Nox(){
        super("eroe Nox", "Nox", 100, 0);
        this.potionsCount = 2;
        this.potionHealAmount = 40;
    }

    @Override
    public boolean usePotion(){
        if(potionsCount > 0 && getCurrentHp() < getMaxHp()) {
            potionsCount--;
            heal(potionHealAmount);
            return true;
        }
            return false;
    }

    @Override
    public int getPotionsCount(){
        return potionsCount;
    }

    public void setPotionsCount(int potionsCount){
        this.potionsCount = Math.max(0, potionsCount);
    }
}
