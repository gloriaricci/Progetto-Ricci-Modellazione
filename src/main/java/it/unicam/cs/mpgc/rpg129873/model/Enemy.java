package it.unicam.cs.mpgc.rpg129873.model;

import java.util.Optional;

public class Enemy extends Entity {
    private String pendingEvent;
    private int levelNumber;
    private String specialEffect;

    public Enemy(String id, String name, int maxHp, int attackPower, int levelNumber, String specialEffect){
        super(id, name, maxHp,attackPower);
        this.levelNumber = levelNumber;
        this.specialEffect = specialEffect;
    }

    public int getLevelNumber(){
        return levelNumber;
    }
    public String getSpecialEffect(){
        return specialEffect;
    }


    public void attack(Hero hero){
        hero.takeDamage(this.getAttackPower());
    }

    protected void setEvent(String message){
        this.pendingEvent = message;
    }

    public Optional<String> consumeEvent(){
        String message = pendingEvent;
        pendingEvent = null;
        return Optional.ofNullable(message);
    }
}
