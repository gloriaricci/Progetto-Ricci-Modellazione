package it.unicam.cs.mpgc.rpg129873;

public class LevelFactory {

    public static Enemy creaBossPerLivello(int numeroLivello){
        switch (numeroLivello){
            case 1:
                return new CollezionistaDiOssa();
            case 2:
                return new MadreBrassmonkey();
            case 3:
                return new IGemelli("Gemello 1");
            case 4:
                return new DuchessaDiSangue();
            case 5:
                return new BaroneShimmer();
            default:
                return null;
        }
    }
}
