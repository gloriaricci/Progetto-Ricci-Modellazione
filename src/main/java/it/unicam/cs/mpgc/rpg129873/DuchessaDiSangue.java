package it.unicam.cs.mpgc.rpg129873;

public class DuchessaDiSangue extends Enemy{
    private int contatoreTurni;

    public DuchessaDiSangue(){
        super("boss4", "Duchessa di Sangue", 220, 30, 4, "furto vitale");
    }

    @Override
    public void attacca(Hero hero){
        contatoreTurni++;

        super.attacca(hero);

        if (contatoreTurni >= 2){
            int rubaVita = 10;
            this.heal(rubaVita);
            contatoreTurni = 0;
        }
    }
}

