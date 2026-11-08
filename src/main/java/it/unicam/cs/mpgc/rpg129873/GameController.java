package it.unicam.cs.mpgc.rpg129873;

public class GameController {

    private Nox player;
    private Level level;
    private int currentLevelNumber;


    public GameController(){
        this.player = new Nox();
        this.player.setWeapon(new Pugnale());
        caricaLivello (1);
    }

    public void caricaLivello(int numero){
        this.currentLevelNumber = numero;
        this.level = LevelFactory.creaLivello(numero);
            System.out.println("Inizio Livello" + numero + ":" + getCurrentBoss().getName());
    }

    public void playerAttack() {
        if (canFight()) {
            player.onTurnStart();
            player.attacca(getCurrentBoss());

            if (getCurrentBoss().isAlive()) {
                getCurrentBoss().attacca(player);
            }
            controllaStatoGioco();
        }
    }

    public void playerUsePotion() {
        if (canFight()) {
            if (player.getCurrentHp() == player.getMaxHp()) {
                System.out.println("La tua vita è già al massimo.");
            } else if (player.usePotion()) {
                System.out.println("Hai usato una pozione. Pozioni rimanenti: " + player.getPotionsCount());
                getCurrentBoss().attacca(player);
            } else {
                System.out.println("Non hai più pozioni di cura.");
            }
            controllaStatoGioco();
        }
    }

    public void playerUseFuria() {
        if (canFight()) {
            if (!player.hasActiveAbility()){
                System.out.println("non hai ancora nessuna abilità attiva");
            }
            player.onTurnStart();
            player.useActiveAbility(getCurrentBoss());

            if (getCurrentBoss().isAlive()) {
                getCurrentBoss().attacca(player);
                }
                controllaStatoGioco();
            }
        }


    public void controllaStatoGioco(){

        if (!player.isAlive()){
            System.out.println("Sei stato sconfitto. Game over.");
            return;
        }

        if (getCurrentBoss().isAlive()) {
            return;
        }

        System.out.println("Hai sconfitto " + getCurrentBoss().getName() + "!");

        if (level.hasNextEnemy()) {
            level.nextEnemy();
            System.out.println("Arriva " + getCurrentBoss().getName() + "!");
            return;
        }

        level.getReward().apply(player);

        if (currentLevelNumber < LevelFactory.getTotalLevels()) {
            caricaLivello(currentLevelNumber + 1);
        } else {
            System.out.println("COMPLIMENTI! Hai sconfitto il Barone dello Shimmer e liberato la città. Hai vinto il gioco!");
        }
    }

    private boolean canFight(){
        return player.isAlive() && getCurrentBoss().isAlive();
    }

    public Nox getPlayer(){
        return player;
    }

    public Enemy getCurrentBoss() {
        return level.getCurrentEnemy();
    }

    public int getCurrentLevel() {
        return currentLevelNumber;
    }
}
