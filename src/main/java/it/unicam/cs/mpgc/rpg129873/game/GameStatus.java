package it.unicam.cs.mpgc.rpg129873.game;

public record GameStatus(
        int levelNumber,
        String heroId,
        String heroName,
        int heroHp,
        int heroMaxHp,
        int potions,
        int maxPotions,
        boolean abilityAvailable,
        String enemyId,
        String enemyName,
        int enemyHp,
        int enemyMaxHp,
        boolean finished,
        boolean won) {
}

