package it.unicam.cs.mpgc.rpg129873.model;

public class Nox extends Hero implements PotionHolder {

    private static final int MAX_POTIONS = 2;

    private int potionsCount;
    private final int potionHealAmount;

    public Nox() {
        super("nox", "Nox", 100, 0);
        this.potionsCount = MAX_POTIONS;
        this.potionHealAmount = 40;
    }

    @Override
    public boolean usePotion() {
        if (potionsCount > 0 && getCurrentHp() < getMaxHp()) {
            potionsCount--;
            heal(potionHealAmount);
            return true;
        }
        return false;
    }

    @Override
    public int getPotionsCount() {
        return potionsCount;
    }

    @Override
    public int getMaxPotions() {
        return MAX_POTIONS;
    }

    public void setPotionsCount(int potionsCount) {
        this.potionsCount = Math.max(0, Math.min(potionsCount, MAX_POTIONS));
    }
}
