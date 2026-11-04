package it.unicam.cs.mpgc.rpg129873;

public class Nox extends Hero{

    private int potionsCount;
    private int potionHealAmount;

    private int level;
    private int experience;
    private int expNextLevel;

    public Nox(){
        super("eroe Nox", "Nox", 100, 0);
        this.potionsCount = 2;
        this.potionHealAmount = 40;
        this.level = 1;
        this.experience = 0;
        this.expNextLevel = 100;
    }

    public void attacca(Enemy enemy){
        if(enemy != null && enemy.isActive()){
            enemy.takeDamage(getAttackPower());
            System.out.println(getName()+ "attacca" + enemy.getName() + "infliggendo" + getAttackPower() + "danni.");
        }
    }

    public boolean usePotion(){
        if(potionsCount > 0){
            potionsCount--;
            System.out.println(getName()+ "Hai usato una pozione di cur. pozioni rimanenti:" + potionsCount);
            return true;
        }else{
            System.out.println("Non hai più pozioni di cura.");
            return false;
        }
    }

    public void aggiornArma(int nuovoAttacco, String nomeArma){
        System.out.println(getName() + " hai una nuova arma: " + nomeArma + ".");
    }

    public int getPotionsCount(){
        return potionsCount;
    }

    public void levelUp(){
        this.level++;
        this.expNextLevel = (int) (this.expNextLevel * 1.5);

        setMaxHp(getMaxHp() + 25);
        setCurrentHp(getMaxHp());

        System.out.println("Nox, sei ora al livello" + level+ "!");
        System.out.println("La tua vita massima è aumentata!");
    }

    public int getLevel(){
        return level;
    }
    public int getExperience(){
        return experience;
    }

    public void gainExp(int amount){
        this.experience += amount;
        System.out.println(getName() + ", hai guadagnato" + amount + "punti esperienza.");
        while (this.experience >= this.expNextLevel){
            this.experience -= this.expNextLevel;
            levelUp();
        }
    }
}
