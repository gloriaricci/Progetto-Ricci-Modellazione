package it.unicam.cs.mpgc.rpg129873.game;

public class LinearHealthProgression implements HealthProgression{

    private static final int BASE_HP = 100;
    private static final int HP_PER_LEVEL = 40;

    @Override
    public int maxHpForLevel(int level){
        if (level < 1) {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
        return BASE_HP + HP_PER_LEVEL * (level - 1);
    }
}
