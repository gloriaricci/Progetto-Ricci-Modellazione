package it.unicam.cs.mpgc.rpg129873;

public class Nox extends Hero{

    private int potionsCount;
    private int potionHealAmount;

    public Nox(){
        super("eroe nox", "Nox", 100, 20, 1.0, 0.0);
        this.potionsCount = 2;
        this.potionHealAmount = 20;
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
}
