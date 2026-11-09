package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.level.Level;
import it.unicam.cs.mpgc.rpg129873.level.LevelFactory;
import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.model.Nox;
import it.unicam.cs.mpgc.rpg129873.weapon.Pugnale;

public class GameController {

    private final GameEventListener listener;
    private Nox player;
    private Level level;
    private int currentLevelNumber;


    public GameController(GameEventListener listener){
        this.listener = listener;
        this.player = new Nox();
        this.player.setWeapon(new Pugnale());
        loadLevel (1);
    }

    public void loadLevel(int number){
        this.currentLevelNumber = number;
        this.level = LevelFactory.createLevel(number);
            listener.onMessage("Inizio Livello" + number + ":" + getCurrentBoss().getName());
    }

    public void playerAttack() {
        if (canFight()) {
            player.onTurnStart();
            player.attack(getCurrentBoss());

            if (getCurrentBoss().isAlive()) {
                getCurrentBoss().attack(player);
            }
            checkGameState();
        }
    }

    public void playerUsePotion() {
        if (canFight()) {
            if (player.getCurrentHp() == player.getMaxHp()) {
                listener.onMessage("La tua vita è già al massimo.");
            } else if (player.usePotion()) {
                listener.onMessage("Hai usato una pozione. Pozioni rimanenti: " + player.getPotionsCount());
                getCurrentBoss().attack(player);
            } else {
                listener.onMessage("Non hai più pozioni di cura.");
            }
            checkGameState();
        }
    }

    public void playerUseFuria() {
        if (canFight()) {
            if (!player.hasActiveAbility()){
                listener.onMessage("non hai ancora nessuna abilità attiva");
            }
            player.onTurnStart();
            player.useActiveAbility(getCurrentBoss());

            if (getCurrentBoss().isAlive()) {
                getCurrentBoss().attack(player);
                }
                checkGameState();
            }
        }


    public void checkGameState(){

        if (!player.isAlive()){
            listener.onMessage("Sei stato sconfitto. Game over.");
            return;
        }

        if (getCurrentBoss().isAlive()) {
            return;
        }

        listener.onMessage("Hai sconfitto " + getCurrentBoss().getName() + "!");

        if (level.hasNextEnemy()) {
            level.nextEnemy();
            listener.onMessage("Arriva " + getCurrentBoss().getName() + "!");
            return;
        }

        level.getReward().apply(player);

        if (currentLevelNumber < LevelFactory.getTotalLevels()) {
            loadLevel(currentLevelNumber + 1);
        } else {
            listener.onMessage("COMPLIMENTI! Hai sconfitto il Barone dello Shimmer e liberato la città. Hai vinto il gioco!");
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

    private void bossAttack(){
        Enemy boss = getCurrentBoss();
        boss.attack(player);
        boss.consumeEvent().ifPresent(listener::onMessage);
    }
}
