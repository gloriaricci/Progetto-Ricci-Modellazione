package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent;
import it.unicam.cs.mpgc.rpg129873.game.GameEventListener;

import java.util.Objects;
import java.util.function.Consumer;

public class GuiEventListener implements GameEventListener {

    private Consumer<GameEvent> handler = event ->{};

    public void setHandler(Consumer <GameEvent> handler){
        this.handler = Objects.requireNonNull(handler);
    }

    @Override
    public void onEvent(GameEvent event){
        handler.accept(event);
    }
}
