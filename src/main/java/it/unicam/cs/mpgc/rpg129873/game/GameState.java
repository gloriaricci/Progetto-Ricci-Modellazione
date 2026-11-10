package it.unicam.cs.mpgc.rpg129873.game;

public record GameState(int levelNumber, int currentHp, int potions) {

    public GameState{
        if (levelNumber < 1){
        throw new IllegalArgumentException("Invalid level: " + levelNumber);
        }
        if (currentHp < 1){
            throw new IllegalArgumentException("Invalid hp: " + currentHp);
        }
        if (potions < 0){
            throw new IllegalArgumentException("Invalid potions: " + potions);
        }
    }
}
