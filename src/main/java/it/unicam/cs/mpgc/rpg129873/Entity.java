package it.unicam.cs.mpgc.rpg129873;

public abstract class Entity {
    private final String id;
    private String name;
    private int maxHp;
    private int currentHp;
    private int attackPower;
    private double speed;
    private boolean active;

    public Entity(String id, String name, int maxHp, int attackPower, double speed){
        if(id == null || id.isBlank()){
            throw new IllegalArgumentException("L'id dell'entità non può essere nullo o vuoto");
        }
        this.id = id;
        this.name = name != null ? name : "Unknow entity";
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attackPower = attackPower;
        this.speed = speed;
        this. active = true;
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getMaxHp(){
        return maxHp;
    }
    public void setMaxHp(int maxHp){
        this.maxHp = maxHp;
    }

    public int getCurrentHp (){
        return currentHp;
    }
    public void setCurrentHp(int currentHp){
        this.currentHp = Math.max (0, Math.min(currentHp, this.maxHp));
    }

    public int getAttackPower(){
        return attackPower;
    }
    public void setAttackPower(int attackPower){
        this.attackPower = attackPower;
    }

    public double getSpeed(){
        return speed;
    }
    public void setSpeed(double speed){
        this.speed = speed;
    }

    public boolean isActive(){
        return active;
    }
    public void setActive (boolean active){
        this.active = active;
    }

    public void takeDamage(int damage){
        this.currentHp = Math.max (0, this.currentHp - damage);
        if (this.currentHp == 0){
            this.active = false;
        }
    }

    public void heal (int amount){
        this.currentHp = Math.min(this.maxHp, this.currentHp + amount);
    }

    public boolean isAlive(){
        return this.currentHp > 0;
    }

    public abstract void update (double deltaTime);
}
