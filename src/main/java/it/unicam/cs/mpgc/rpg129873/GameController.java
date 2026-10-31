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
        if (currentBoss != null) {
            System.out.println("Inizio Livello" + livello + ":" + currentBoss.getName());
        }
    }

    public void playerAttack(){
        if (player.isAlive() && currentBoss != null && currentBoss.isActive()){
            player.attacca((currentBoss));

            if (currentBoss.isActive()){
                currentBoss.attacca(player);
            }

            controllaStatoGioco();
        }
    }

    public void playerUsePotion(){
        if (player.isAlive()&& currentBoss != null && currentBoss.isActive()){
            if (player.usePotion()){
                player.heal(20);
                currentBoss.attacca(player);
            }
            controllaStatoGioco();
        }
    }

    public void controllaStatoGioco(){
        if (currentBoss == null){
            return;
        }
        if (!currentBoss.isActive()){
            System.out.println("Hai sconfitto" + currentBoss.getName()+ "!!");

            player.gainExp(50);

            if (currentLevel == 3 && currentBoss.getName().equals("Gemello 1")){
                System.out.println("Rimane un gemello da sconfiggere...");
                currentBoss = new IGemelli("Gemello 2");
                return;
            }
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
