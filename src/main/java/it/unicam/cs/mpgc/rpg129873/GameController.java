package it.unicam.cs.mpgc.rpg129873;

public class GameController {

    private Nox player;
    private Enemy currentBoss;
    private int currentLevel;
    private static final int max_level = 5;

    public GameController(){
        this.player = new Nox();
        this.currentLevel = 1;
        caricaLivello (currentLevel);
    }

    public void caricaLivello(int livello){
        this.currentBoss = LevelFactory.creaBossPerLivello(livello);
        System.out.println("Inizio Livello" + livello + ":" + currentBoss.getName());
    }

    public void controllaStatoGioco(){
        if (currentBoss == null){
            return;
        }
        if (!currentBoss.isActive()){
            System.out.println("Hai sconfitto" + currentBoss.getName()+ "!!");
            if (currentLevel < max_level){
                currentLevel++;
                caricaLivello(currentLevel);
            }else{
                System.out.println("HAI VINTO");
            }
        }
    }

    public Nox getPlayer(){
        return player;
    }

    public Enemy getCurrentBoss(){
        return currentBoss;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }
}
