package it.unicam.cs.mpgc.rpg129873;

public class Nox extends Hero{

    public Nox(){
        super("eroe nox", "Nox", 100, 20, 1.0, 0.0);
    }

    public void attacca(Enemy enemy){
        if(enemy != null && enemy.isActive()){
            enemy.takeDamage(getAttackPower());
            System.out.println(getName()+ "attacca" + enemy.getName() + "infliggendo" + getAttackPower() + "danni.");
        }
    }
}
