package it.unicam.cs.mpgc.rpg129873.level;

public interface LevelProvider {
    Level createLevel (int number);
    int getTotalLevels();
}
