package it.unicam.cs.mpgc.rpg129873;

public class Enemy extends Entity{
    private int levelNumber;
    private String specialEffect;

    public Enemy(String id, String name, int maxHp, int attackPower, double speed, int levelNumber, String specialEffect){
        super(id, name, maxHp,attackPower, speed);
        this.levelNumber = levelNumber;
        this.specialEffect = specialEffect;
    }

    public int getLevelNumber(){
        return levelNumber;
    }
    public String getSpecialEffect(){
        return specialEffect;
    }


    public void attacca(Nox hero){
        hero.takeDamage(this.getAttackPower());
    }
}
