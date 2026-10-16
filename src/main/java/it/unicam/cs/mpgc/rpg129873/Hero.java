package it.unicam.cs.mpgc.rpg129873;

public class Hero extends Entity {
    private double x;

    public Hero(String id, String name, int maxHp,int attackPower, double speed, double initialX){
        super(id, name, maxHp,attackPower,speed);
        this.x = initialX;
    }

    public double getX(){
        return x;
    }
    public void setX(double x){
        this.x = x;
    }

    public void dash(double distance){
        this.x += distance;
    }

    @Override
    public void update(double deltaTime){

    }
}

