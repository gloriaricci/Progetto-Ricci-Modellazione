package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent;
import it.unicam.cs.mpgc.rpg129873.game.GameEventListener;

public class ConsoleEventListener implements GameEventListener {

    private final EventTextFormatter formatter = new EventTextFormatter();

    @Override
    public void onEvent(GameEvent event){
        System.out.println(formatter.format(event));
    }
}
