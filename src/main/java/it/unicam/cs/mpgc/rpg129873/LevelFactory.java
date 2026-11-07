package it.unicam.cs.mpgc.rpg129873;

import java.util.List;

public class LevelFactory {

    public static Level creaLivello(int numeroLivello){
        switch (numeroLivello){
            case 1:
                return new Level(1, List.of(new CollezionistaDiOssa()));
            case 2:
                return new Level(2, List.of(new MadreBrassmonkey()));
            case 3:
                return new Level(3, List.of(
                        new IGemelli("boss3a", "Gemello 1"),
                        new IGemelli("boss3b", "Gemello 2")));
            case 4:
                return new Level(4, List.of(new DuchessaDiSangue()));
            case 5:
                return new Level(5, List.of(new BaroneShimmer()));
            default:
                throw new IllegalArgumentException("Livello non valido: " + numeroLivello);
        }
    }

    public static void ApplicaRicompensa(int livelloCompleto, Hero hero){
        switch (livelloCompleto){
            case 1:
                hero.unlockScattoRapido();
                System.out.println("Fine primo livello: hai sbloccato lo Scatto Rapido!");
                break;
            case 2:
                hero.setWeapon(new Spada());
                System.out.println("Fine secondo livello: hai ottenuto la Spada!");
                break;
            case 3:
                hero.setWeapon(new SpadaTemplari());
                hero.unlockCuraDeiTemplari();
                System.out.println("Fine terzo livello: hai ottenuto la Grande Spada dei Templari e la sua abilità di cura!");
                break;
            case 4:
                hero.unlockFuriaShimmer();
                System.out.println("Fine quarto livello: hai sbloccato l'abilità della Furia Shimmer!");
                break;
            case 5:
                System.out.println("COMPLIMENTI! Hai sconfitto il Barone dell Shimmer e liberato la città. Hai vinto il gioco!");
                break;
            default:
                break;
        }
    }
}
