package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent;
import it.unicam.cs.mpgc.rpg129873.game.GameEventListener;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Collega {@code Game} alla vista. La {@code GameFactory} riceve il listener quando viene
 * creata, ma la {@code GameView} esiste solo quando parte una partita: il listener resta
 * quindi lo stesso e il gestore viene sostituito a ogni nuova partita. Finché non ne viene
 * impostato uno, gli eventi vengono ignorati.
 */
public class GuiEventListener implements GameEventListener {

    private Consumer<GameEvent> handler = event -> {
    };

    public void setHandler(Consumer<GameEvent> handler) {
        this.handler = Objects.requireNonNull(handler);
    }

    @Override
    public void onEvent(GameEvent event) {
        handler.accept(event);
    }
}
