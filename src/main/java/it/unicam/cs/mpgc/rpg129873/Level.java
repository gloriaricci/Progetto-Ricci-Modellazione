package it.unicam.cs.mpgc.rpg129873;

import java.util.List;

public class Level {

    private final int number;
    private final List<Enemy> enemies;
    private final Reward reward;
    private int currentIndex = 0;

    public Level(int number, List<Enemy> enemies, Reward reward){
        this.number = number;
        this.enemies = enemies;
        this.reward = reward;
    }

    public int getNumber(){
        return number;
    }

    public Reward getReward(){
        return reward;
    }

    public Enemy getCurrentEnemy(){
        return enemies.get(currentIndex);
    }

    public boolean hasNextEnemy(){
        return currentIndex < enemies.size() - 1;
    }

    public void nextEnemy(){
        currentIndex++;
    }
}
