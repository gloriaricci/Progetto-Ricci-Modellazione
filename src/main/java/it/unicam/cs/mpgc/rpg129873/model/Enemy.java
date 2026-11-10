package it.unicam.cs.mpgc.rpg129873.model;

import java.util.Optional;

public class Enemy extends Entity {
    private String pendingEvent;

    public Enemy(String id, String name, int maxHp, int attackPower){
        super(id, name, maxHp,attackPower);
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
