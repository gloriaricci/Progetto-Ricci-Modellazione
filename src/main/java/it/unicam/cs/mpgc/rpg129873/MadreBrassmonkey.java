package it.unicam.cs.mpgc.rpg129873;

public class MadreBrassmonkey extends Enemy{
    private int turniAttacco = 0;

    public MadreBrassmonkey(){
        super("boss2", "Madre Brassmonkey", 140, 20,1.1, 2,"Schermo nero");
    }

    @Override
    public void attacca(Nox hero){
        turniAttacco++;
        super.attacca(hero);

        if (turniAttacco <= 2){
            //effetto speciale da aggiungere schermo nero
            turniAttacco = 0;
        }
    }
}

