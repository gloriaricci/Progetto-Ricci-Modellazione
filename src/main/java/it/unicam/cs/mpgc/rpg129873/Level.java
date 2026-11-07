package it.unicam.cs.mpgc.rpg129873;

import java.util.List;

public class Level {

    private final int number;
    private final List<Enemy> enemies;
    private int currentIndex = 0;

    public Level(int number, List<Enemy> enemies){
        this.number = number;
        this.enemies = enemies;
    }

    public int getNumber(){
        return number;
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
