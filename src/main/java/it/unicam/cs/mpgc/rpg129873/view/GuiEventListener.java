package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.controller.GameEventListener;

import java.util.Objects;
import java.util.function.Consumer;

public class GuiEventListener implements GameEventListener {

    private Consumer<String> handler = message ->{};

    public void setHandler(Consumer <String> handler){
        this.handler = Objects.requireNonNull(handler);
    }

    @Override
    public void onMessage(String message){
        handler.accept(message);
    }
}
