package it.unicam.cs.mpgc.rpg129873.game;

public record GameStatus(
        int levelNumber,
        String heroName,
        int heroHp,
        int heroMaxHp,
        int potions,
        boolean abilityAvailable,
        String enemyId,
        String enemyName,
        int enemyHp,
        int enemyMaxHp,
        boolean finished,
        boolean won) {
}
