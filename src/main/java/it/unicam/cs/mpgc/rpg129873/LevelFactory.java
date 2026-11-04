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
