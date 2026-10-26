package it.unicam.cs.mpgc.rpg129873;

public class LevelFactory {

    public static Enemy creaBossPerLivello(int numeroLivello){
        switch (numeroLivello){
            case 1:
                return new Enemy("boss1", "CollezionistaDiOssa", 100, 15, 1.0,1, "Nessun effettp");
            case 2:
                return new Enemy("boss2", "MadreBrassmokey", 140, 20,1.1, 2,"Schermo nero");
            case 3:
                return new Enemy("boss3-4", "I gemelli", 100,17,1.2,3,"Nessun effetto");
            case 4:
                return new Enemy("boss5", "Duchessa di Sangue", 220, 30,1.4,4,"Furto vitale");
            case 5:
                return new BaroneShimmer();
            default:
                return null;
        }
    }
}
