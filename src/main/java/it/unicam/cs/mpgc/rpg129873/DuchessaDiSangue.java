package it.unicam.cs.mpgc.rpg129873;

public class DuchessaDiSangue extends Enemy{
    private int contatoreTurni;

    public DuchessaDiSangue(){
        super("boss5", "Duchessa di Sangue", 220, 30, 1.4, 4, "furto vitale");
    }

    @Override
    public void attacca(Nox hero){
        contatoreTurni++;
        hero.takeDamage(getAttackPower());
        if (contatoreTurni >= 2){
            int rubaVita = 10;
            this.heal(rubaVita);
            contatoreTurni = 0;
        }
    }
}

