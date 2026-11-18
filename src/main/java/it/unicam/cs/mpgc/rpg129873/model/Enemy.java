package it.unicam.cs.mpgc.rpg129873.model;

import java.util.Optional;

public class Enemy extends Entity {
    private EnemyEffect pendingEvent;

    public Enemy(String id, String name, int maxHp, int attackPower){
        super(id, name, maxHp,attackPower);
    }

    public void attack(Hero hero){
        hero.takeDamage(this.getAttackPower());
    }

    protected void setEvent(EnemyEffect effect){
        this.pendingEvent = effect;
    }

    public Optional<EnemyEffect> consumeEvent(){
        EnemyEffect effect = pendingEvent;
        pendingEvent = null;
        return Optional.ofNullable(effect);
    }
}
